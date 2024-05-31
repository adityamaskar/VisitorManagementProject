package com.aditya.visitorproject.service;

import com.aditya.visitorproject.repo.UserAdressesRepo;
import com.aditya.visitorproject.repo.UserProfileRepo;
import com.aditya.visitorproject.repo.UserVehicleRepo;
import com.aditya.visitorproject.dtos.AuthCompleteEvent;
import com.aditya.visitorproject.dtos.UserAddressesDTO;
import com.aditya.visitorproject.dtos.UserProfileDto;
import com.aditya.visitorproject.dtos.VehiclesDTO;
import com.aditya.visitorproject.entity.UserAddresses;
import com.aditya.visitorproject.entity.UserProfile;
import com.aditya.visitorproject.entity.Vehicles;
import com.aditya.visitorproject.repo.UserVisitTrackerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileService implements UserDetailsService {

    @Autowired
    private UserProfileRepo userProfileRepo;

    @Autowired
    private UserAdressesRepo userAdressesRepo;

    @Autowired
    private UserVehicleRepo userVehicleRepo;

    @Autowired
    private UserVisitTrackerRepo userVisitTrackerRepo;

    @Autowired
    private KafkaTemplate<String, AuthCompleteEvent> kafkaTemplate;

    private final String regexForNumber = "^\\+\\d{7,}$";


    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);


    public List<String> getAllProfiles() {
        List<UserProfile> listOfUsers = userProfileRepo.findAll();
//        List<String> userList = new ArrayList<>();

//        for (UserProfile userProfile : listOfUsers) {
//            userList.add(userProfile.getUserName());
//        }

//       with Stream API
        List<String> list = listOfUsers.stream().map(o -> o.getUserName()).toList();
        return list;
    }

    public UserDetails loadUserByUsername(String userName) {
        UserProfile byUserName;
        try {
            byUserName = userProfileRepo.findByUserName(userName);
            return new User(byUserName.getUserName(), byUserName.getPassword(), new ArrayList<>());
        }catch (NullPointerException exception){
            logger.error("Username or Password is wrong");
            throw new RuntimeException("Username or Password is wrong");
        }
    }

    public List<UserProfile> getAllProfilesWithPass() {
        List<UserProfile> listOfUsers = userProfileRepo.findAll();
        return listOfUsers;
    }

    public UserProfile getUserbyId(Long userId) {
        Optional<UserProfile> byId = userProfileRepo.findById(userId);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            throw new RuntimeException("User " + userId + " doesn't exist.");
        }
    }

    public UserProfile getUserByUserName(String userName) {
        UserProfile byUserName = userProfileRepo.findByUserName(userName);
        if (byUserName != null) {
            return byUserName;
        } else {
            throw new RuntimeException("User " + userName + " doesn't exist.");
        }
    }


    public Long createUser(UserProfileDto userProfileDto) {
        verifyUserProfile(userProfileDto);
        //creating the Address Entity
        List<UserAddressesDTO> userAddressesDTO = userProfileDto.getUserAddressesDTO();
        List<UserAddresses> userAddressesList = null;
        if (userAddressesDTO != null) {
            userAddressesList = userAddressesDTO.stream().map(x -> UserAddressesDTO.userAddressDTOtoUserAddress(x)).collect(Collectors.toList());
        } else {
            logger.warn("Address is Empty");
        }

        //Creating the Vehicles Entity
        List<VehiclesDTO> vehiclesDTOS = userProfileDto.getVehiclesDTOS();
        List<Vehicles> vehiclesList = null;
        if (vehiclesDTOS != null) {
            vehiclesList = vehiclesDTOS.stream().map(x -> VehiclesDTO.vehicleDTOtoVehicle(x)).collect(Collectors.toList());
        } else {
            logger.warn("Vehicle is Empty");
        }

        UserProfile newUser = UserProfile.builder()
                .userName(userProfileDto.getUserName())
                .firstName(userProfileDto.getFirstName())
                .lastName(userProfileDto.getLastName())
                .gender(userProfileDto.getGender())
                .email(userProfileDto.getEmail())
                .phoneNum(userProfileDto.getPhoneNum())
                .password(userProfileDto.getPassword())
                .userAddresses(userAddressesList)
                .vehiclesList(vehiclesList)
                .build();

        UserProfile newUserSaved = userProfileRepo.save(newUser);
        return newUserSaved.getId();
    }

    private void verifyUserProfile(UserProfileDto userProfileDto) {
        UserProfile byUserName = userProfileRepo.findByUserName(userProfileDto.getUserName());
        if (byUserName != null) {
            throw new RuntimeException("UserName already exists");
        }
        UserProfile byemail = userProfileRepo.findByemail(userProfileDto.getEmail());
        if (byemail != null) {
            throw new RuntimeException("Email already exists");
        }
        UserProfile byPhoneNum = userProfileRepo.findByPhoneNum(userProfileDto.getPhoneNum());
        if (byPhoneNum != null) {
            throw new RuntimeException("Phone Number already exists");
        }
        if(!userProfileDto.getPhoneNum().matches(regexForNumber)){
            throw new RuntimeException("Please provide correct Phone number");
        }
    }

    public void sendNotificationOnAuth(String userName){
        UserProfile userByUserName = getUserByUserName(userName);
        kafkaTemplate.send(kafkaTemplate.getDefaultTopic(),
                new AuthCompleteEvent(userName, userByUserName.getFirstName() + " " + userByUserName.getLastName()));
    }


}
