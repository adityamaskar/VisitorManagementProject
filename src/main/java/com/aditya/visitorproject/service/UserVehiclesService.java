package com.aditya.visitorproject.service;

import com.aditya.visitorproject.dtos.VehiclesDTO;
import com.aditya.visitorproject.repo.UserProfileRepo;
import com.aditya.visitorproject.repo.UserVehicleRepo;
import com.aditya.visitorproject.entity.UserProfile;
import com.aditya.visitorproject.entity.Vehicles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserVehiclesService {

    @Autowired
    UserVehicleRepo userVehicleRepo;

    @Autowired
    UserProfileRepo userProfileRepo;

    public String setNewVehicle(VehiclesDTO vehiclesDTO, String username) {
        UserProfile byUserName = userProfileRepo.findByUserName(username);
        boolean isVehicleNameAvailable = byUserName.getVehiclesList().stream().map(x -> x.getVehicleName()).anyMatch(x -> x.equalsIgnoreCase(vehiclesDTO.getVehicleName()));
        if (!isVehicleNameAvailable) {
            String vehicleName = vehiclesDTO.getVehicleName();
            List<Vehicles> vehiclesFromDB = byUserName.getVehiclesList();
//            UserAddresses userVehicles = UserAddressesDTO.userAddressDTOtoUserAddress(userAddressesDTO);
            Vehicles vehicles = VehiclesDTO.vehicleDTOtoVehicle(vehiclesDTO);
            vehiclesFromDB.add(vehicles);
//            userAddressesFromDB.add(userAddresses);
//            byUserName.setUserAddresses(userAddressesFromDB);
            byUserName.setVehiclesList(vehiclesFromDB);
            userProfileRepo.save(byUserName);
            return vehicleName;
        } else {
            throw new RuntimeException("Address name is already taken");
        }
    }
}
