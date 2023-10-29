package com.visitorproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Vehicles {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String VehicleName;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private String VehicleNum;

    private boolean isCab;
}
