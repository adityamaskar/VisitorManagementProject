package com.visitorproject.service;

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


    public Map<String, String> searchSociety(String firstName, String phoneNum, String societyName, String addressName) {
        UserProfile byPhoneNum = userProfileRepo.findByPhoneNum(phoneNum);
        Map<String, String> userInformation = new HashMap<>();
        if (byPhoneNum == null) {
            throw new RuntimeException("This Phone Number : '" + phoneNum + "' not assigned with the User");
        }
        List<UserAddresses> userAddresses = byPhoneNum.getUserAddresses();
//        String selectedSociety = null;

        if (byPhoneNum.getFirstName().equalsIgnoreCase(firstName.toLowerCase().trim())) {
            List<UserAddresses> userWithSameSociety = userAddresses.stream().filter(x -> x.getSocietyName().equalsIgnoreCase(societyName)).collect(Collectors.toList());
            if (userWithSameSociety.isEmpty()) {
                throw new RuntimeException("This Society Name : '" + societyName + "' is not correct");
            }
            if (addressName != null && !addressName.equalsIgnoreCase("undefined")) {
                List<UserAddresses> userWithSameAddressName = userAddresses.stream().filter(x -> x.getAddressName().equalsIgnoreCase(addressName)).collect(Collectors.toList());
                if (!userWithSameAddressName.isEmpty()) {
                    userInformation.put("result", "found");
                    userInformation.put("firstName", byPhoneNum.getFirstName());
                    userInformation.put("lastName", byPhoneNum.getLastName());
                    userInformation.put("societyAvailableWithInfo", userWithSameAddressName.get(0).toString());
                    userInformation.put("message", "Society Details as Below");
                    return userInformation;
                } else {
                    if (userWithSameSociety.size() == 1) {
                        userInformation.put("result", "found");
                        userInformation.put("firstName", byPhoneNum.getFirstName());
                        userInformation.put("lastName", byPhoneNum.getLastName());
                        userInformation.put("message", "Found Above address if the required address not available please contact owner to add it");

                        userInformation.put("societyAvailableWithInfo", userWithSameSociety.toString());
                        return userInformation;
                    } else {
                        userInformation.put("result", "multiple Found");
                        userInformation.put("firstName", byPhoneNum.getFirstName());
                        userInformation.put("lastName", byPhoneNum.getLastName());
                        userInformation.put("message", "Address Name is incorrect, Below are the available addresses with the Society Name, " +
                                "If desired addresses not there connect owner to Add it");

                        userInformation.put("societyAvailableWithInfo", userWithSameSociety.toString());
                        return userInformation;
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

        return userInformation;
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
