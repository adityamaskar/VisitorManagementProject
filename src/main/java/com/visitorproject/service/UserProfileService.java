package com.visitorproject.service;

import com.visitorproject.dtos.UserAddressesDTO;
import com.visitorproject.dtos.UserProfileDto;
import com.visitorproject.entity.UserAddresses;
import com.visitorproject.entity.UserProfile;
import com.visitorproject.repo.UserAdressesRepo;
import com.visitorproject.repo.UserProfileRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService implements UserDetailsService {

    @Autowired
    UserProfileRepo userProfileRepo;

    @Autowired
    UserAdressesRepo userAdressesRepo;

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);


    public List<String> getAllProfiles() {
        List<UserProfile> listOfUsers = userProfileRepo.findAll();
        List<String> userList = new ArrayList<>();
        for (UserProfile userProfile : listOfUsers) {
            userList.add(userProfile.getUserName());
        }
        return userList;
    }

    public UserDetails loadUserByUsername(String userName) {
        UserProfile byUserName = userProfileRepo.findByUserName(userName);
        return new User(byUserName.getUserName(), byUserName.getPassword(), new ArrayList<>());
    }

    public List<UserProfile> getAllProfilesWithPass() {
        List<UserProfile> listOfUsers = userProfileRepo.findAll();
        return listOfUsers;
    }

    public UserProfile getUserbyId(Integer userId) {
        Optional<UserProfile> byId = userProfileRepo.findById(userId);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            throw new RuntimeException("User " + userId + " doesn't exist.");
        }
    }

    public Integer createUser(UserProfileDto userProfileDto) {
        verifyUserProfile(userProfileDto);
        UserProfile newUser = UserProfile.builder()
                .userName(userProfileDto.getUserName())
                .firstName(userProfileDto.getFirstName())
                .lastName(userProfileDto.getLastName())
                .email(userProfileDto.getEmail())
                .phoneNum(userProfileDto.getPhoneNum())
                .password(userProfileDto.getPassword())
                .build();
        UserProfile newUserSaved = userProfileRepo.save(newUser);
        return newUserSaved.getId();
    }

    private void verifyUserProfile(UserProfileDto userProfileDto) {
        UserProfile byUserName = userProfileRepo.findByUserName(userProfileDto.getUserName());
        if (byUserName != null) {
            throw new RuntimeException("User already exists");
        }
        UserProfile byemail = userProfileRepo.findByemail(userProfileDto.getEmail());
        if (byemail != null) {
            throw new RuntimeException("Email already exists");
        }
        UserProfile byPhoneNum = userProfileRepo.findByPhoneNum(userProfileDto.getPhoneNum());
        if (byPhoneNum != null) {
            throw new RuntimeException("Phone Number already exists");
        }
    }


}

