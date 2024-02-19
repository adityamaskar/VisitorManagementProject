package com.visitorproject.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthCompleteEvent {
    private String username;

    private String fullName;
}
