package com.aditya.visitorproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Vehicles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleName;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private String vehicleNum;

    private Boolean isCab;
}
