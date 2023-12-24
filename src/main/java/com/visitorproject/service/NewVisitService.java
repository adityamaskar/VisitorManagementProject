package com.visitorproject.service;

import com.visitorproject.dtos.SearchUserInfoDTO;
import com.visitorproject.dtos.UserAddressesDTO;
import com.visitorproject.dtos.VisitTrackerDTO;
import com.visitorproject.entity.UserAddresses;
import com.visitorproject.entity.UserProfile;
import com.visitorproject.entity.Vehicles;
import com.visitorproject.entity.VisitTracker;
import com.visitorproject.repo.UserAdressesRepo;
import com.visitorproject.repo.UserProfileRepo;
import com.visitorproject.repo.UserVehicleRepo;
import com.visitorproject.repo.UserVisitTrackerRepo;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.context.Theme;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewVisitService {

    @Autowired
    private UserAdressesRepo userAdressesRepo;

    @Autowired
    private UserProfileRepo userProfileRepo;

    @Autowired
    private UserVehicleRepo userVehicleRepo;

    @Autowired
    private UserVisitTrackerRepo userVisitTrackerRepo;


    public SearchUserInfoDTO searchSociety(String firstName, String phoneNum, String societyName, String addressName, String currentUsername) {
        UserProfile byPhoneNum = userProfileRepo.findByPhoneNum(phoneNum);

//        Map<String, String> userInformation = new HashMap<>();
        SearchUserInfoDTO searchUserInfoDTO = new SearchUserInfoDTO();
        if (byPhoneNum == null) {
            throw new RuntimeException("This Phone Number : '" + phoneNum + "' not assigned with the User");
        }
        if (byPhoneNum.getUserName().equalsIgnoreCase(currentUsername)) {
            throw new RuntimeException("You are trying to enter your own details : Phone Number ");
        }
        List<UserAddresses> userAddresses = byPhoneNum.getUserAddresses();
//        String selectedSociety = null;

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
                            .societyAvailableWithInfo(userWithSameAddressName.stream().map(x -> UserAddressesDTO.userAddressesToUserAddressDTO(x)).collect(Collectors.toList()))
                            .build();

                    return userInfoDTO;
                } else {
                    if (userWithSameSociety.size() == 1) {
                        SearchUserInfoDTO userInfoDTO = searchUserInfoDTO.builder().message("Found Above address if the required address not available please contact owner to add it")
                                .result("found").lastName(byPhoneNum.getLastName())
                                .firstName(byPhoneNum.getFirstName())
                                .societyAvailableWithInfo(userWithSameSociety.stream().map(x -> UserAddressesDTO.userAddressesToUserAddressDTO(x)).collect(Collectors.toList()))
                                .build();

                        return userInfoDTO;
                    } else {
                        SearchUserInfoDTO userInfoDTO = searchUserInfoDTO.builder().message("Address Name is incorrect, Below are the available addresses with the Society Name, \" +\n" +
                                        "                                \"If desired addresses not there connect owner to Add it")
                                .result("multiple found").lastName(byPhoneNum.getLastName())
                                .firstName(byPhoneNum.getFirstName())
                                .societyAvailableWithInfo(userWithSameSociety.stream().map(x -> UserAddressesDTO.userAddressesToUserAddressDTO(x)).collect(Collectors.toList()))
                                .build();

                        return userInfoDTO;
                    }
                }
            }
        } else {
            throw new RuntimeException("FirstName : '" + firstName + "' is not registered with given user");
        }
//        String message = "";
//        List<String> societyList = new ArrayList<>();
//        List<UserAddresses> userAddresseslist = new ArrayList<>();
//        UserAddresses selectedUserAddress = null;
//        for (UserAddresses userAddresses : byPhoneNum.getUserAddresses()) {
//            societyList.add(userAddresses.getSocietyName());
//            userAddresseslist.add(userAddresses);
//            if (addressName == null) {
//                if (userAddresses.getSocietyName().equalsIgnoreCase(societyName)) {
//                    selectedUserAddress = userAddresses;
//                }
//            }
//            {
//                if (userAddresses.getSocietyName().equalsIgnoreCase(societyName) && userAddresses.getAddressName().equalsIgnoreCase(addressName)) {
//                    selectedUserAddress = userAddresses;
//                }
//            }
//        }

//        if (selectedUserAddress == null) {
//            userInformation.put("message", "Society Name is incorrect please check from the list or contact the person");
//            userInformation.put("societyAvailable", societyList.toString());
//            userInformation.put("societyAvailableWithInfo", userAddresseslist.toString());
////            userInformation.put("Id", so)
//        } else if (societyList.isEmpty()) {
//            userInformation.put("societyAvailable", "No Societies Found");
//        } else {
//            userInformation.put("SocietyName", societyName);
//            userInformation.put("societyAvailableWithInfo", selectedUserAddress.toString());
//            userInformation.put("Username", byPhoneNum.getUserName());
//        }

        return searchUserInfoDTO;
    }

    public void setVisit(String visitorUsername, String ownerUsername, VisitTrackerDTO visitTrackerDTO) {
        validateIMPData(visitorUsername, ownerUsername, visitTrackerDTO);

        UserProfile visitorProfile = userProfileRepo.findByUserName(visitorUsername);
        UserProfile ownerProfile = userProfileRepo.findByUserName(ownerUsername);
        UserAddresses userAddresses;
        try {
            userAddresses = ownerProfile.getUserAddresses().stream().filter(x -> x.getAddressName().equalsIgnoreCase(visitTrackerDTO.getAddressName())).collect(Collectors.toList()).get(0);
            if (visitTrackerDTO.getIsVehiclePresent() == true) {
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
                    .visitorVehicleName(visitTrackerDTO.getVisitorVehicleName()).build();

            VisitTracker save = userVisitTrackerRepo.save(visitTrackerObj);
            visitTrackerObj.setAuthCode(generateUniqueAuthNumber());
            visitTrackerObj.setOwnerApproval(false);
        } catch (Exception ex) {
            throw new RuntimeException("Something Went wrong while saving object");
        }

    }

    public static String generateUniqueAuthNumber() {
        UUID uuid = UUID.randomUUID();
        String uniqueNumber = "EN" + uuid.toString().replace("-", "").substring(0, 6);
        return uniqueNumber;
    }

    private void validateIMPData(String visitorUsername, String ownerUsername, VisitTrackerDTO visitTrackerDTO) {
        if (visitorUsername == ownerUsername) {
            throw new RuntimeException("Owner and Visitor can't be same");
        }
        if (visitTrackerDTO.getVisitDateTime() == null && visitTrackerDTO.getExitDateTime() == null) {
            throw new RuntimeException("Visit or exit time can't be null");
        }
        if (LocalDateTime.now().isAfter(visitTrackerDTO.getVisitDateTime())) {
            throw new RuntimeException("Visit time can't be in past");
        }
        if (!visitTrackerDTO.getVisitDateTime().isBefore(visitTrackerDTO.getExitDateTime())) {
            throw new RuntimeException("Exit time should be After the visit time");
        }
        if (visitTrackerDTO.getVisitType() == null) {
            throw new RuntimeException("Please specify Visit type");
        }
        if (visitTrackerDTO.getIsVehiclePresent() == true && visitTrackerDTO.getVisitorVehicleName() == null) {
            throw new RuntimeException("Vehicle name is empty");
        }
        if (visitTrackerDTO.getIsVehiclePresent() == false && visitTrackerDTO.getVisitorVehicleName() != null) {
            throw new RuntimeException("You have selected vehicle as not present but provided vehicle name");
        }
    }
}
