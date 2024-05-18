package com.visitorproject.service;

import com.visitorproject.dtos.AuthCompleteEvent;
import com.visitorproject.dtos.UserAddressesDTO;
import com.visitorproject.dtos.UserProfileDto;
import com.visitorproject.dtos.VehiclesDTO;
import com.visitorproject.entity.UserAddresses;
import com.visitorproject.entity.UserProfile;
import com.visitorproject.entity.Vehicles;
import com.visitorproject.repo.UserProfileRepo;
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
    private KafkaTemplate<String, AuthCompleteEvent> kafkaTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    public List<String> getAllProfiles() {
        List<UserProfile> listOfUsers = userProfileRepo.findAll();
        List<String> list = listOfUsers.stream().map(o -> o.getUserName()).toList();
        return list;
    }

    public UserDetails loadUserByUsername(String userName) {
        UserProfile byUserName;
            byUserName = userProfileRepo.findByUserName(userName);
            return new User(byUserName.getUserName(), byUserName.getPassword(), new ArrayList<>());
    }

    public List<UserProfile> getAllProfilesWithPass() {
        return userProfileRepo.findAll();
    }

    public UserProfile getUserById(Long userId) {
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

    public synchronized Long createUser(UserProfileDto userProfileDto) {
        verifyUserProfile(userProfileDto);
        List<UserAddressesDTO> userAddressesDTO = userProfileDto.getUserAddressesDTO();
        List<UserAddresses> userAddressesList = null;
        if (userAddressesDTO != null) {
            userAddressesList = userAddressesDTO.stream().map(x -> UserAddressesDTO.userAddressDTOtoUserAddress(x)).collect(Collectors.toList());
        } else {
            logger.warn("Address object is Empty");
        }

        //Creating the Vehicles Entity
        List<VehiclesDTO> vehiclesDTOS = userProfileDto.getVehiclesDTOS();
        List<Vehicles> vehiclesList = null;
        if (vehiclesDTOS != null) {
            vehiclesList = vehiclesDTOS.stream().map(x -> VehiclesDTO.vehicleDTOtoVehicle(x)).collect(Collectors.toList());
        } else {
            logger.warn("Vehicle object is Empty");
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
        UserProfile byEmail = userProfileRepo.findByemail(userProfileDto.getEmail());
        if (byEmail != null) {
            throw new RuntimeException("Email already exists");
        }
        UserProfile byPhoneNum = userProfileRepo.findByPhoneNum(userProfileDto.getPhoneNum());
        if (byPhoneNum != null) {
            throw new RuntimeException("Phone Number already exists");
        }
        String regexForNumber = "^\\+\\d{7,}$";
        if(!userProfileDto.getPhoneNum().matches(regexForNumber)){
            throw new RuntimeException("Please provide correct Phone number with country code");
        }
    }

    public void sendNotificationOnAuth(String userName){
        //todo correct in the code level
        UserProfile userByUserName = getUserByUserName(userName);
        kafkaTemplate.send(kafkaTemplate.getDefaultTopic(),
                new AuthCompleteEvent(userName, userByUserName.getFirstName() + " " + userByUserName.getLastName()));
    }
}

