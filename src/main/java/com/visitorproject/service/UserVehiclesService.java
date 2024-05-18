package com.visitorproject.service;

import com.visitorproject.dtos.VehiclesDTO;
import com.visitorproject.entity.UserProfile;
import com.visitorproject.entity.Vehicles;
import com.visitorproject.repo.UserProfileRepo;
import com.visitorproject.repo.UserVehicleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserVehiclesService {

    @Autowired
    UserVehicleRepo userVehicleRepo;

    @Autowired
    UserProfileRepo userProfileRepo;

    public synchronized String setNewVehicle(VehiclesDTO vehiclesDTO, String username) {
        UserProfile byUserName = userProfileRepo.findByUserName(username);
        boolean isVehicleNameAvailable = byUserName.getVehiclesList().stream().map(x -> x.getVehicleName()).anyMatch(x -> x.equalsIgnoreCase(vehiclesDTO.getVehicleName()));
        if (!isVehicleNameAvailable) {
            String vehicleName = vehiclesDTO.getVehicleName();
            List<Vehicles> vehiclesFromDB = byUserName.getVehiclesList();
            Vehicles vehicles = VehiclesDTO.vehicleDTOtoVehicle(vehiclesDTO);
            vehiclesFromDB.add(vehicles);
            byUserName.setVehiclesList(vehiclesFromDB);
            userProfileRepo.save(byUserName);
            return vehicleName;
        } else {
            throw new RuntimeException("Address name is already taken");
        }
    }
}
