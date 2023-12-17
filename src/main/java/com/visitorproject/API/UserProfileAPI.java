package com.visitorproject.API;

import com.visitorproject.dtos.UserAddressesDTO;
import com.visitorproject.dtos.UserProfileDto;
import com.visitorproject.dtos.VehiclesDTO;
import com.visitorproject.dtos.VisitTrackerDTO;
import com.visitorproject.entity.AuthRequest;
import com.visitorproject.entity.UserProfile;
import com.visitorproject.service.NewVisitService;
import com.visitorproject.service.UserAddressesService;
import com.visitorproject.service.UserProfileService;
import com.visitorproject.service.UserVehiclesService;
import com.visitorproject.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
//@CrossOrigin(origins = "http://localhost:5173")
public class UserProfileAPI {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserAddressesService userAddressesService;

    @Autowired
    private UserVehiclesService userVehiclesService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private NewVisitService newVisitService;

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

    @GetMapping("username/{username}")
    public UserProfile getUserViaJWT(@PathVariable String username) {
        return userProfileService.getUserByUserName(username);
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
        String result = userAddressesService.setNewAddress(userAddressesDTO, username);
        return " Address " + result + " SAVED";
    }

    @PostMapping("/set-vehicle-details")
    public String SetNewVehicle(@RequestHeader("Authorization") String authorizationHeader, @RequestBody VehiclesDTO vehiclesDTO) {
        String token = extractJwtToken(authorizationHeader);
        String username = getUsername(token);
        String result = userVehiclesService.setNewVehicle(vehiclesDTO, username);
        return result;
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

    @GetMapping("/search-home")
    public Map<String, String> searchHome(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String firstName, @RequestParam String phoneNum, @RequestParam String societyName, @RequestParam(required = false) String addressName) {
        String token = extractJwtToken(authorizationHeader);
        String currentUsername = getUsername(token);
        Map<String, String> searchedSociety = newVisitService.searchSociety(firstName, phoneNum, societyName, addressName, currentUsername);
        return searchedSociety;
    }

    @PostMapping("set-visit")
    public void setVisit(@RequestHeader("Authorization") String authorizationHeader, @RequestBody VisitTrackerDTO visitTrackerDTO, @RequestParam String phoneNum) {
        String token = extractJwtToken(authorizationHeader);
        String username = getUsername(token);
        newVisitService.setVisit(visitTrackerDTO, phoneNum, username);
    }


}
