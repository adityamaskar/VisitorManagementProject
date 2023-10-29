package com.visitorproject.dtos;

import com.visitorproject.entity.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Table
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class VehiclesDTO {

    private String VehicleName;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private String VehicleNum;

    private boolean isCab;
}
