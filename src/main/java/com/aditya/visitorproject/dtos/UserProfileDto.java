package com.aditya.visitorproject.dtos;

import com.aditya.visitorproject.entity.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class UserProfileDto {

    private String userName;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String password;

    private String email;

    private String phoneNum;

    private List<UserAddressesDTO> userAddressesDTO;

    private List<VehiclesDTO> vehiclesDTOS;

//    public static UserProfileDto userProfiletoUserProfileDTO(UserProfile userProfile) {
//        UserProfileDto userProfileDto = UserProfileDto.builder().email(userProfile.getEmail())
//                .userAddressesDTO(UserAddressesDTO.userAddressesToUserAddressDTO(userProfile.getUserAddresses()))
//                .userName(userProfile.getUserName())
//                .firstName(userProfile.getFirstName())
//                .lastName(userProfile.getLastName())
//                .password(userProfile.getPassword())
//                .phoneNum(userProfile.getPhoneNum()).build();
//
//        return userProfileDto;
//    }

//    public static UserProfile userProfileDTOtoUserProfile(UserProfileDto userProfileDto) {
//        UserProfile userProfile = UserProfile.builder().email(userProfileDto.getEmail())
//                .userAddresses(userProfileDto.getUserAddresses())
//                .userName(userProfileDto.getUserName())
//                .firstName(userProfileDto.getFirstName())
//                .lastName(userProfileDto.getLastName())
//                .password(userProfileDto.getPassword())
//                .phoneNum(userProfileDto.getPhoneNum()).build();
//        return userProfile;
//    }
}
