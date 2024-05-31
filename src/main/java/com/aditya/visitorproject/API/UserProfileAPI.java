package com.aditya.visitorproject.API;

import com.aditya.visitorproject.dtos.*;
import com.aditya.visitorproject.service.NewVisitService;
import com.aditya.visitorproject.service.UserProfileService;
import com.visitorproject.dtos.*;
import com.aditya.visitorproject.entity.AuthRequest;
import com.aditya.visitorproject.entity.UserProfile;
import com.aditya.visitorproject.filter.JwtService;
import com.aditya.visitorproject.service.UserAddressesService;
import com.aditya.visitorproject.service.UserVehiclesService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*") // if using gateway then keep commented otherwise uncomment.
//@CrossOrigin(origins = "http://localhost:5173")
public class UserProfileAPI {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserAddressesService userAddressesService;

    @Autowired
    private UserVehiclesService userVehiclesService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private NewVisitService newVisitService;

    @Autowired
    private JwtService jwtHelper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
        return jwtHelper.extractUsername(token);
    }


    private String extractJwtToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Extract the token excluding "Bearer "
        }
        throw new IllegalArgumentException("Invalid or missing Authorization header");
    }

    @PostMapping("/authenticate")
//    @CircuitBreaker(name = "authentication" , fallbackMethod = "fallbackAuth")
//    @TimeLimiter(name = "authentication")
//    @Retry(name = "authentication")
    public ResponseEntity<String> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtHelper.generateToken(authRequest.getUserName());
//                userProfileService.sendNotificationOnAuth(authRequest.getUserName());
                logger.info("User  " + '"' + authRequest.getUserName() + '"' + " is authenticated");
                return new ResponseEntity<>(token, HttpStatus.ACCEPTED);
            } else {
                throw new UsernameNotFoundException("Invalid user request !");
            }
        }catch (InternalAuthenticationServiceException exception){
            logger.error(exception.getMessage() + " : " + exception);
            throw new IllegalArgumentException("Username or Password is wrong");
        }
        catch (Exception exception){
            logger.error(exception.getMessage() + " : " + exception);
            if(exception.getMessage().equalsIgnoreCase("Bad credentials")){
                throw new IllegalArgumentException("Username or Password is wrong");
            }else {
                throw new RuntimeException("Some error occurred");
            }
        }
    }


    public String fallbackAuth(AuthRequest authRequest, RuntimeException runtimeException){
        if(runtimeException.getMessage().equalsIgnoreCase("Username or Password is wrong")){
            return "Username or Password is wrong";
        }
        return "Oops some Error occurred try after some time";
    }

    @GetMapping("/search-home")
    public SearchUserInfoDTO searchHome(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String firstName, @RequestParam String phoneNum, @RequestParam String societyName, @RequestParam(required = false) String addressName) throws RuntimeException{
        String token = extractJwtToken(authorizationHeader);
        String currentUsername = getUsername(token);
        SearchUserInfoDTO searchedSociety = newVisitService.searchSociety(firstName, phoneNum, societyName, addressName, currentUsername);
        return searchedSociety;
    }

    @PostMapping("/set-visit")
    public void setVisit(@RequestParam String visitorUsername, @RequestParam String ownerUsername, @RequestBody VisitTrackerDTO visitTrackerDTO) {
//        String token = extractJwtToken(authorizationHeader);
//        String username = getUsername(token);
        newVisitService.setVisit(visitorUsername, ownerUsername, visitTrackerDTO);
    }

    @GetMapping("/received-approval-requests")
    public List<VisitTrackerDTO> receivedApprovalRequest(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractJwtToken(authorizationHeader);
        String username = getUsername(token);
        List<VisitTrackerDTO> pendingrequestsForApproval = newVisitService.getPendingrequestsForApproval(username);
        return pendingrequestsForApproval;
    }

    @GetMapping("/approved-requests-pending-visit")
    public List<VisitTrackerDTO> approvedRequestsWhichArePending(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractJwtToken(authorizationHeader);
        String username = getUsername(token);
        List<VisitTrackerDTO> approvedRequestsWhichArePending = newVisitService.approvedRequestsWhichArePending(username);
        return approvedRequestsWhichArePending;
    }

    @PostMapping("/handle-approval-request")
    public String handleApprovalRequest( @RequestBody VisitTrackerDTO visitTrackerDTO){
        System.out.println(visitTrackerDTO);
        return newVisitService.handleApprovalRequest(visitTrackerDTO);
    }

    @PostMapping("/handle-rejection-request")
    public String handleRejectionRequest( @RequestBody VisitTrackerDTO visitTrackerDTO){
        System.out.println(visitTrackerDTO);
        return newVisitService.handleRejectionRequest(visitTrackerDTO);
    }

    @GetMapping("/get-my-requsted-visits")
    public List<VisitTrackerDTO> getMyRequestedVisits(@RequestHeader("Authorization") String authorizationHeader){
        String token = extractJwtToken(authorizationHeader);
        String username = getUsername(token);
        List <VisitTrackerDTO> visitTrackerDTOS =  newVisitService.getMyRequestedVisits(username);
        return  visitTrackerDTOS;
    }



//    @GetMapping("/test-micro")
//    @CircuitBreaker(name = "test1", fallbackMethod = "fallbackMethod1")
//    @TimeLimiter(name = "test1")
//    @Retry(name = "test1")
//    public CompletableFuture<String> testMicro() {
//        return CompletableFuture.supplyAsync(this::testMicroString);
//    }
//
//    public String testMicroString() {
//        return "I am working fine";
//    }

    // Fallback method for circuit breaker
//    public CompletableFuture<String> fallbackMethod1(RuntimeException runtimeException) {
//        return CompletableFuture.completedFuture("Fallback method triggered: " + runtimeException.getMessage());
//    }
}
