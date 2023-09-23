package com.visitorproject.API;

import com.visitorproject.entity.UserProfile;
import com.visitorproject.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserProfileAPI {

    @Autowired
    UserProfileService userProfileService;

    @GetMapping("/all")
    public List<UserProfile> getAllUsers() {
        List<UserProfile> allProfiles = userProfileService.getAllProfiles();
        return allProfiles;
    }

    @GetMapping("/test")
    public String test() {
        return "running";
    }
}
