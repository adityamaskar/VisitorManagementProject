package com.visitorproject.dtos;

import com.visitorproject.entity.VehicleType;
import com.visitorproject.entity.Vehicles;
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

    private String vehicleName;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private String vehicleNum;

    private Boolean isCab;

    public static Vehicles vehicleDTOtoVehicle(VehiclesDTO vehiclesDTO) {
        Vehicles vehicles = Vehicles.builder().vehicleNum(vehiclesDTO.getVehicleNum())
                .vehicleName(vehiclesDTO.getVehicleName())
                .vehicleType(vehiclesDTO.getVehicleType())
                .isCab(vehiclesDTO.getIsCab())
                .build();
        return vehicles;
    }

    public static VehiclesDTO vehicleToVehicleDTO(Vehicles vehicles) {
        VehiclesDTO vehiclesDTO = VehiclesDTO.builder().vehicleNum(vehicles.getVehicleNum())
                .vehicleName(vehicles.getVehicleName())
                .vehicleType(vehicles.getVehicleType())
                .isCab(vehicles.getIsCab())
                .build();
        return vehiclesDTO;
    }
}
