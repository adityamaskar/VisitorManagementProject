package com.visitorproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table
@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
@Builder
public class UserAddresses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

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
