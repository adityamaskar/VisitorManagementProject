package com.visitorproject.service;

import com.visitorproject.dtos.SearchUserInfoDTO;
import com.visitorproject.dtos.UserAddressesDTO;
import com.visitorproject.dtos.VisitTrackerDTO;
import com.visitorproject.entity.UserAddresses;
import com.visitorproject.entity.UserProfile;
import com.visitorproject.entity.VisitTracker;
import com.visitorproject.entity.VisitType;
import com.visitorproject.repo.UserProfileRepo;
import com.visitorproject.repo.UserVisitTrackerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewVisitService {

    @Autowired
    private UserProfileRepo userProfileRepo;

    @Autowired
    private UserVisitTrackerRepo userVisitTrackerRepo;


    public SearchUserInfoDTO searchSociety(String firstName, String phoneNum, String societyName, String addressName, String currentUsername) {
        UserProfile byPhoneNum = userProfileRepo.findByPhoneNum(phoneNum);

        SearchUserInfoDTO searchUserInfoDTO = new SearchUserInfoDTO();
        if (byPhoneNum == null) {
            throw new RuntimeException("This Phone Number : '" + phoneNum + "' not assigned with the User");
        }
        if (byPhoneNum.getUserName().equalsIgnoreCase(currentUsername)) {
            throw new RuntimeException("You are trying to enter your own details : Phone Number ");
        }
        List<UserAddresses> userAddresses = byPhoneNum.getUserAddresses();

        if (byPhoneNum.getFirstName().equalsIgnoreCase(firstName.toLowerCase().trim())) {
            List<UserAddresses> userWithSameSociety = userAddresses.stream().filter(x -> x.getSocietyName().equalsIgnoreCase(societyName)).collect(Collectors.toList());
            if (userWithSameSociety.isEmpty()) {
                throw new RuntimeException("This Society Name : '" + societyName + "' is not correct");
            }
            if (addressName != null || addressName == "undefined" && !addressName.equalsIgnoreCase("undefined")) {
                List<UserAddresses> userWithSameAddressName = userAddresses.stream().filter(x -> x.getAddressName().equalsIgnoreCase(addressName)).collect(Collectors.toList());
                if (!userWithSameAddressName.isEmpty()) {
                    SearchUserInfoDTO userInfoDTO = searchUserInfoDTO.builder().message("Society Details as Below")
                            .result("found").lastName(byPhoneNum.getLastName())
                            .firstName(byPhoneNum.getFirstName())
                            .userName(byPhoneNum.getUserName())
                            .societyAvailableWithInfo(userWithSameAddressName.stream().map(x -> UserAddressesDTO.userAddressesToUserAddressDTO(x)).collect(Collectors.toList()))
                            .build();

                    return userInfoDTO;
                } else {
                    if (userWithSameSociety.size() == 1) {
                        SearchUserInfoDTO userInfoDTO = searchUserInfoDTO.builder().message("Found Above address if the required address not available please contact owner to add it")
                                .result("found").lastName(byPhoneNum.getLastName())
                                .firstName(byPhoneNum.getFirstName())
                                .userName(byPhoneNum.getUserName())
                                .societyAvailableWithInfo(userWithSameSociety.stream().map(x -> UserAddressesDTO.userAddressesToUserAddressDTO(x)).collect(Collectors.toList()))
                                .build();

                        return userInfoDTO;
                    } else {
                        SearchUserInfoDTO userInfoDTO = searchUserInfoDTO.builder().message("Address Name is incorrect, Below are the available addresses with the Society Name, \" +\n" +
                                        "                                \"If desired addresses not there connect owner to Add it")
                                .result("multiple found").lastName(byPhoneNum.getLastName())
                                .firstName(byPhoneNum.getFirstName())
                                .userName(byPhoneNum.getUserName())
                                .societyAvailableWithInfo(userWithSameSociety.stream().map(x -> UserAddressesDTO.userAddressesToUserAddressDTO(x)).collect(Collectors.toList()))
                                .build();

                        return userInfoDTO;
                    }
                }
            }
        } else {
            throw new RuntimeException("FirstName : '" + firstName + "' is not registered with given user");
        }

        return searchUserInfoDTO;
    }

    public void setVisit(String visitorUsername, String ownerUsername, VisitTrackerDTO visitTrackerDTO) {
        validateIMPData(visitorUsername, ownerUsername, visitTrackerDTO);

        UserProfile visitorProfile = userProfileRepo.findByUserName(visitorUsername);
        UserProfile ownerProfile = userProfileRepo.findByUserName(ownerUsername);
        UserAddresses userAddresses;
        try {
            userAddresses = ownerProfile.getUserAddresses().stream().filter(x -> x.getAddressName().equalsIgnoreCase(visitTrackerDTO.getAddressName())).collect(Collectors.toList()).get(0);
            if (visitTrackerDTO.getIsVehiclePresent()) {
                visitorProfile.getVehiclesList().stream().filter(x -> x.getVehicleName().equalsIgnoreCase(visitTrackerDTO.getVisitorVehicleName())).collect(Collectors.toList()).get(0);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Issue in the User Address or the Visitor vehicle details");
        }

        try {
            VisitTracker visitTrackerObj = VisitTracker.builder().visitorUsername(visitorUsername)
                    .version(1)
                    .visitorUsername(visitorUsername)
                    .ownerUsername(ownerUsername)
                    .ownerAddressId(userAddresses.getId())
                    .visitDateTime(visitTrackerDTO.getVisitDateTime())
                    .exitDateTime(visitTrackerDTO.getExitDateTime())
                    .dateTimeOfVisitSchedule(LocalDateTime.now())
                    .visitType(visitTrackerDTO.getVisitType())
                    .NumberOfVisitors(visitTrackerDTO.getNumberOfVisitors())
                    .isVehiclePresent(visitTrackerDTO.getIsVehiclePresent())
                    .visitorVehicleName(visitTrackerDTO.getVisitorVehicleName())
                    .build();

            VisitTracker save = userVisitTrackerRepo.save(visitTrackerObj);
            visitTrackerObj.setOwnerApproval(false);
            userVisitTrackerRepo.save(visitTrackerObj);
        } catch (Exception ex) {
            throw new RuntimeException("Something Went wrong while saving object");
        }

    }

    //todo auth code part
    public static String generateUniqueAuthNumber() {
        UUID uuid = UUID.randomUUID();
        String uniqueNumber = "EN" + uuid.toString().replace("-", "").substring(0, 6);
        return uniqueNumber;
    }

    private void validateIMPData(String visitorUsername, String ownerUsername, VisitTrackerDTO visitTrackerDTO) {
        if (visitorUsername.equals(ownerUsername)) {
            throw new RuntimeException("Owner and Visitor can't be same");
        }
        if(visitTrackerDTO.getVisitType().equals(VisitType.None)){
            throw new RuntimeException("Select the Visit Type");
        }
        if (visitTrackerDTO.getVisitDateTime() == null || visitTrackerDTO.getExitDateTime() == null) {
            throw new RuntimeException("Visit or Exit time can't be null");
        }
        if (LocalDateTime.now().isAfter(visitTrackerDTO.getVisitDateTime())) {
            throw new RuntimeException("Visit Time can't be in past");
        }
        if (!visitTrackerDTO.getVisitDateTime().isBefore(visitTrackerDTO.getExitDateTime())) {
            throw new RuntimeException("Exit Time should be After the visit time");
        }
        if (visitTrackerDTO.getVisitType() == null) {
            throw new RuntimeException("Please specify Visit type");
        }
        if (visitTrackerDTO.getIsVehiclePresent() && visitTrackerDTO.getVisitorVehicleName() == null) {
            throw new RuntimeException("Vehicle name is empty");
        }
        if (!visitTrackerDTO.getIsVehiclePresent() && visitTrackerDTO.getVisitorVehicleName() != null) {
            throw new RuntimeException("You have selected vehicle as not present but provided vehicle name");
        }
    }

    public List<VisitTrackerDTO> getPendingRequestsForApproval(String ownerUsername) {

        List<VisitTracker> byOwnerUsernameAndOwnerApprovalIsFalse = userVisitTrackerRepo.findByOwnerUsernameAndOwnerApprovalIsFalseAndRejectionTimeIsNull(ownerUsername);
        List<VisitTrackerDTO> listResultDTO = new ArrayList<>();

        for (VisitTracker visitTracker : byOwnerUsernameAndOwnerApprovalIsFalse) {
            UserProfile byUserNameVisitor = userProfileRepo.findByUserName(visitTracker.getVisitorUsername());
            UserProfile byUserNameOwner = userProfileRepo.findByUserName(visitTracker.getOwnerUsername());
            String addressName = byUserNameOwner.getUserAddresses().stream().filter(x -> x.getId() == visitTracker.getOwnerAddressId()).collect(Collectors.toList()).get(0).getAddressName();

            VisitTrackerDTO resultDTO = VisitTrackerDTO.fromVisitorTrackerToVisitorTrackerDTO(visitTracker);
            resultDTO.setFullName(byUserNameVisitor.getFirstName() + " " + byUserNameVisitor.getLastName());
            resultDTO.setAddressName(addressName);

            listResultDTO.add(resultDTO);
        }
        return listResultDTO;
    }

    public List<VisitTrackerDTO> approvedRequestsWhichArePending(String ownerUsername) {
        List<VisitTracker> byOwnerUsernameAndOwnerApprovalIsTrue = userVisitTrackerRepo.findByOwnerUsernameAndOwnerApprovalIsTrueAndRejectionTimeIsNotNull(ownerUsername);
        List<VisitTrackerDTO> listResultDTO = new ArrayList<>();

        for (VisitTracker visitTracker : byOwnerUsernameAndOwnerApprovalIsTrue) {
            UserProfile byUserNameVisitor = userProfileRepo.findByUserName(visitTracker.getVisitorUsername());
            UserProfile byUserNameOwner = userProfileRepo.findByUserName(visitTracker.getOwnerUsername());
            String addressName = byUserNameOwner.getUserAddresses().stream().filter(x -> x.getId() == visitTracker.getOwnerAddressId()).collect(Collectors.toList()).get(0).getAddressName();

            VisitTrackerDTO resultDTO = VisitTrackerDTO.fromVisitorTrackerToVisitorTrackerDTO(visitTracker);
            resultDTO.setFullName(byUserNameVisitor.getFirstName() + " " + byUserNameVisitor.getLastName());
            resultDTO.setAddressName(addressName);

            listResultDTO.add(resultDTO);
        }
        return listResultDTO;
    }

    public String handleApprovalRequest(VisitTrackerDTO visitTrackerDTO) {
        if(visitTrackerDTO.getExtraManualComments() != null && !visitTrackerDTO.getOwnerApproval() && visitTrackerDTO.getApprovalOrRejectionTime() == null) {
            List<VisitTracker> byOwnerUsernameAndVisitorUsername = userVisitTrackerRepo.findByOwnerUsernameAndVisitorUsername(visitTrackerDTO.getOwnerUsername(), visitTrackerDTO.getVisitorUsername());
            VisitTracker visitTracker = null;
            if(byOwnerUsernameAndVisitorUsername.size() > 1){
                visitTracker = byOwnerUsernameAndVisitorUsername.stream().filter((x) -> x.getVisitDateTime().equals(visitTrackerDTO.getVisitDateTime())).collect(Collectors.toList()).get(0);
            }
            else {
                visitTracker = byOwnerUsernameAndVisitorUsername.get(0);
            }
            visitTracker.setExtraManualComments(visitTrackerDTO.getExtraManualComments());
            visitTracker.setOwnerApproval(true);
            visitTracker.setVersion(visitTracker.getVersion() +1 );
            visitTracker.setApprovalOrRejectionTime(LocalDateTime.now());
            userVisitTrackerRepo.save(visitTracker);
            return "Processed";
        }
        return "Not Processed";
    }

    public String handleRejectionRequest(VisitTrackerDTO visitTrackerDTO) {
        List<VisitTracker> byOwnerUsernameAndVisitorUsername = userVisitTrackerRepo.findByOwnerUsernameAndVisitorUsername(visitTrackerDTO.getOwnerUsername(), visitTrackerDTO.getVisitorUsername());
        VisitTracker visitTracker = null;
        if(byOwnerUsernameAndVisitorUsername.size() > 1){
            visitTracker = byOwnerUsernameAndVisitorUsername.stream().filter((x) -> x.getVisitDateTime().equals(visitTrackerDTO.getVisitDateTime())).collect(Collectors.toList()).get(0);
        }
        else {
            visitTracker = byOwnerUsernameAndVisitorUsername.get(0);
        }
        if( !visitTracker.getOwnerApproval() && visitTracker.getApprovalOrRejectionTime() == null) {
            visitTracker.setRejectionReason(visitTrackerDTO.getRejectionReason());
            visitTracker.setOwnerApproval(false);
            visitTracker.setVersion(visitTracker.getVersion() +1 );
            visitTracker.setApprovalOrRejectionTime(LocalDateTime.now());
            userVisitTrackerRepo.save(visitTracker);
            return "Processed";
        }
        return "Not Processed";
    }

    public List<VisitTrackerDTO> getMyRequestedVisits(String username) {
        List<VisitTracker> byVisitorUsername = userVisitTrackerRepo.findByVisitorUsername(username);

        return byVisitorUsername.stream().map(VisitTrackerDTO::fromVisitorTrackerToVisitorTrackerDTO)
                .peek(x -> {
                    String s = userProfileRepo.findByUserName(x.getOwnerUsername()).getFirstName() + " " +
                            userProfileRepo.findByUserName(x.getOwnerUsername()).getLastName();
                    x.setFullName(s);
                })
                .sorted(Comparator.comparing(VisitTrackerDTO::getDateTimeOfVisitSchedule)).toList();
    }
}
