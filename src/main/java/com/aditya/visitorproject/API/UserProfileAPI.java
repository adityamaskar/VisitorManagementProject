package com.aditya.visitorproject.API;

import com.aditya.visitorproject.dtos.UserAddressesDTO;
import com.aditya.visitorproject.dtos.UserProfileDto;
import com.aditya.visitorproject.dtos.VehiclesDTO;
import com.aditya.visitorproject.entity.AuthRequest;
import com.aditya.visitorproject.entity.UserProfile;
import com.aditya.visitorproject.filter.JwtService;
import com.aditya.visitorproject.service.UserAddressesService;
import com.aditya.visitorproject.service.UserProfileService;
import com.aditya.visitorproject.service.UserVehiclesService;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;


@RestController
@RequestMapping("/user")
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
    private JwtService jwtHelper;

    @Value("${kafta-notifications-on-auth}")
    private boolean kafkaNotificationOnAuth;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/all-usernames")
    public List<String> getAllUsers() {
        return userProfileService.getAllProfiles();
    }

    @GetMapping("/all-username-password")
    public List<UserProfile> getAllUsersAndPass() {
        return userProfileService.getAllProfilesWithPass();
    }

    @GetMapping("/{userId}")
    public UserProfile getUser(@PathVariable Long userId) {
        return userProfileService.getUserById(userId);
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
        return userVehiclesService.setNewVehicle(vehiclesDTO, username);
    }

    @GetMapping("/test/{token}")
    public String getUsername(@PathVariable String token) {
        return jwtHelper.extractUsername(token);
    }

    public String extractJwtToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Extract the token excluding "Bearer "
        }
        throw new IllegalArgumentException("Invalid or missing Authorization header");
    }

    @PostMapping("/authenticate")
    @CircuitBreaker(name = "authentication" , fallbackMethod = "fallbackAuth")
    @TimeLimiter(name = "authentication")
    public CompletableFuture<ResponseEntity<String>> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        return CompletableFuture.supplyAsync(() -> {
            Authentication authentication = null;
            try {
//                Thread.sleep(10000); added to check the timelimter check, for circuitbreaker we can directly enter wrong password multiple times.
                try {
                    authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
                }catch (NullPointerException exception){
                    logger.error("Username or Password is wrong");
                    throw new RuntimeException("Username or Password is wrong");
                }
                if (authentication.isAuthenticated()) {
                    String token = jwtHelper.generateToken(authRequest.getUserName());

                    if(kafkaNotificationOnAuth)
                        userProfileService.sendNotificationOnAuth(authRequest.getUserName());

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
        });
    }

    public CompletableFuture<ResponseEntity<String>> fallbackAuth(AuthRequest authRequest, Exception ex){
        return CompletableFuture.supplyAsync(() -> {
            if(ex instanceof TimeoutException)
                return new ResponseEntity<>("Request Taking Longer than expected please try after some time", HttpStatus.INTERNAL_SERVER_ERROR);
            else if (ex instanceof CallNotPermittedException) {
                return new ResponseEntity<>("Too Many Requests, Please try after some time", HttpStatus.TOO_MANY_REQUESTS);
            }
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
        });
    }
}
