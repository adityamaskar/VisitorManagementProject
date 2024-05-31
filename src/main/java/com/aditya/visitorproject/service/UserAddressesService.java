package com.aditya.visitorproject.service;

import com.aditya.visitorproject.repo.UserAdressesRepo;
import com.aditya.visitorproject.repo.UserProfileRepo;
import com.aditya.visitorproject.dtos.UserAddressesDTO;
import com.aditya.visitorproject.entity.UserAddresses;
import com.aditya.visitorproject.entity.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAddressesService {

    @Autowired
    private UserAdressesRepo userAdressesRepo;

    @Autowired
    private UserProfileRepo userProfileRepo;


    public String setNewAddress(UserAddressesDTO userAddressesDTO, String username) {
        UserProfile byUserName = userProfileRepo.findByUserName(username);
        boolean isUserAddressNameAvailable = byUserName.getUserAddresses().stream().map(x -> x.getAddressName()).anyMatch(x -> x.equalsIgnoreCase(userAddressesDTO.getAddressName()));
        if (!isUserAddressNameAvailable) {
            String addressName = userAddressesDTO.getAddressName();
            List<UserAddresses> userAddressesFromDB = byUserName.getUserAddresses();
            UserAddresses userAddresses = UserAddressesDTO.userAddressDTOtoUserAddress(userAddressesDTO);
            userAddressesFromDB.add(userAddresses);
            byUserName.setUserAddresses(userAddressesFromDB);
            userProfileRepo.save(byUserName);
            return addressName;
        } else {
            throw new RuntimeException("Address name is already taken");
        }
    }

}
