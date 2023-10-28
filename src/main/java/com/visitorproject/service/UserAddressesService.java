package com.visitorproject.service;

import com.visitorproject.dtos.UserAddressesDTO;
import com.visitorproject.dtos.UserProfileDto;
import com.visitorproject.entity.UserAddresses;
import com.visitorproject.entity.UserProfile;
import com.visitorproject.repo.UserAdressesRepo;
import com.visitorproject.repo.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAddressesService {

    @Autowired
    private UserAdressesRepo userAdressesRepo;

    @Autowired
    private UserProfileRepo userProfileRepo;


    public void setNewAddress(UserAddressesDTO userAddressesDTO, String username) {
        UserProfile byUserName = userProfileRepo.findByUserName(username);
        UserAddresses userAddresses = UserAddressesDTO.userAddressDTOtoUserAddress(userAddressesDTO);

        List<UserAddresses> userAddressesList = new ArrayList<>();
        userAddressesList.add(userAddresses);
        byUserName.setUserAddresses(userAddressesList);
        userProfileRepo.save(byUserName);
    }

}
