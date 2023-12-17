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
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.context.Theme;

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

    public void setVisit(VisitTrackerDTO visitTrackerDTO, String phoneNum, String username) {
        UserProfile byPhoneNum = userProfileRepo.findByPhoneNum(phoneNum);
        UserAddresses addressByAddressId = userAdressesRepo.findById(visitTrackerDTO.getOwnerAddressId()).get();
        Vehicles visitorVehicle = userVehicleRepo.findById(visitTrackerDTO.getVisitorVehicleId()).get();
        if (byPhoneNum == null) {
            throw new RuntimeException("user mobile num incorrect");
        }
        //continue this--------
//        if(byPhoneNum.)
        VisitTracker visitTrackerObj = VisitTracker.builder().visitorUsername(username)
                .ownerUsername(byPhoneNum.getUserName())
                .ownerAddressId(visitTrackerDTO.getOwnerAddressId())
                .isVehiclePresent(visitTrackerDTO.getIsVehiclePresent())
                .visitorVehicleId(visitTrackerDTO.getVisitorVehicleId())
                .isShortVisit(visitTrackerDTO.getIsShortVisit())
                .visitDateTime(visitTrackerDTO.getVisitDateTime())
                .exitDateTime(visitTrackerDTO.getExitDateTime())
                .NumberOfVisitors(visitTrackerDTO.getNumberOfVisitors()).build();
    }
}
