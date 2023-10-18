package com.visitorproject.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    private Integer phoneNum;
}
