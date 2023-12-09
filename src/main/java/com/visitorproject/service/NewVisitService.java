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
        if (byPhoneNum == null) {
            throw new RuntimeException("This " + phoneNum + " not assigned with the User");
        }
//        String selectedSociety = null;
        if (byPhoneNum.getFirstName().equals(firstName.toLowerCase().trim())) {
//            for (UserAddresses userAddress : byPhoneNum.getUserAddresses()) {
//                if (userAddress.getSocietyName().equals(societyName.toLowerCase().trim())) {
//
//                    break;
//                }
//            }
        } else {
            throw new RuntimeException("This " + firstName + " with given number not assigned with the User");
        }
        String message = "";
        List<String> societyList = new ArrayList<>();
        List<UserAddresses> userAddresseslist = new ArrayList<>();
        UserAddresses selectedUserAddress = null;
        for (UserAddresses userAddresses : byPhoneNum.getUserAddresses()) {
            societyList.add(userAddresses.getSocietyName());
            userAddresseslist.add(userAddresses);
            if (addressName == null) {
                if (userAddresses.getSocietyName().equalsIgnoreCase(societyName)) {
                    selectedUserAddress = userAddresses;
                }
            }
            {
                if (userAddresses.getSocietyName().equalsIgnoreCase(societyName) && userAddresses.getAddressName().equalsIgnoreCase(addressName)) {
                    selectedUserAddress = userAddresses;
                }
            }
        }
        Map<String, String> userInformation = new HashMap<>();
        userInformation.put("firstName", byPhoneNum.getFirstName());
        userInformation.put("lastName", byPhoneNum.getLastName());

        if (selectedUserAddress == null) {
            userInformation.put("message", "Society Name is incorrect please check from the list or contact the person");
            userInformation.put("societyAvailable", societyList.toString());
            userInformation.put("societyAvailableWithInfo", userAddresseslist.toString());
//            userInformation.put("Id", so)
        } else if (societyList.isEmpty()) {
            userInformation.put("societyAvailable", "No Societies Found");
        } else {
            userInformation.put("SocietyName", societyName);
            userInformation.put("societyAvailableWithInfo", selectedUserAddress.toString());
            userInformation.put("Username", byPhoneNum.getUserName());
        }

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
