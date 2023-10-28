package com.visitorproject.API;

import com.visitorproject.dtos.UserAddressesDTO;
import com.visitorproject.dtos.UserProfileDto;
import com.visitorproject.entity.AuthRequest;
import com.visitorproject.entity.UserProfile;
import com.visitorproject.service.UserAddressesService;
import com.visitorproject.service.UserProfileService;
import com.visitorproject.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserProfileAPI {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserAddressesService userAddressesService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping("/all-usernames")
    public List<String> getAllUsers() {
        List<String> allProfiles = userProfileService.getAllProfiles();
        return allProfiles;
    }

    @GetMapping("/all-username-password")
    public List<UserProfile> getAllUsersAndPass() {
        List<UserProfile> allProfiles = userProfileService.getAllProfilesWithPass();
        return allProfiles;
    }

    @GetMapping("/{userId}")
    public UserProfile getUser(@PathVariable Long userId) {
        return userProfileService.getUserbyId(userId);
    }

    @PostMapping("/new-user")
    public String createNewUser(@RequestBody UserProfileDto userProfileDto) {
        Long user = userProfileService.createUser(userProfileDto);
        return "User with ID " + user + " is created";
    }

    @PostMapping("/set-address")
    public String SetNewAddress(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserAddressesDTO userAddressesDTO) {
        String token = extractJwtToken(authorizationHeader);
        String username = getUsername(token);
        userAddressesService.setNewAddress(userAddressesDTO, username);
        return "SAVED";
    }

    @GetMapping("/test/{token}")
    public String getUsername(@PathVariable String token) {
        return jwtUtil.extractUsername(token);
    }

    private String extractJwtToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Extract the token excluding "Bearer "
        }
        throw new IllegalArgumentException("Invalid or missing Authorization header");
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getUserName());
    }
}
