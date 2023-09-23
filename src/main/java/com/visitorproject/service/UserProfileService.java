package com.visitorproject.service;

import com.visitorproject.entity.UserProfile;
import com.visitorproject.repo.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {

    @Autowired
    UserProfileRepo userProfileRepo;

    public List<UserProfile> getAllProfiles(){
        List<UserProfile> listOfUsers = userProfileRepo.findAll();
        return listOfUsers;
    }
}

