package com.visitorproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class SearchUserInfoDTO {
    private String result;
    private String firstName;
    private String lastName;
    private List<UserAddressesDTO> societyAvailableWithInfo;
    private String message;
    private String userName;

}
