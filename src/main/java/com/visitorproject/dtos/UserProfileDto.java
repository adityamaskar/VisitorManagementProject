package com.visitorproject.dtos;

import com.visitorproject.entity.UserProfile;
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

    private String password;

    private String email;

    private String phoneNum;

    public List<UserAddressesDTO> userAddressesDTO;

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
