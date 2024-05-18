package com.visitorproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Builder
public class UserAddresses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String addressName;

    private Boolean defaultAddress;

    private Boolean societyRegistered;

    private String societyName;

    private String wing;

    private String flatNumber;

    private String streetOrArea;

    private String city;

    private String district;

    private String pinCode;

}
