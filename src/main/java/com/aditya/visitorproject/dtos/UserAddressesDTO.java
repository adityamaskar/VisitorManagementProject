package com.aditya.visitorproject.dtos;

import com.aditya.visitorproject.entity.UserAddresses;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class UserAddressesDTO {

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

    public static UserAddressesDTO userAddressesToUserAddressDTO(UserAddresses userAddresses) {
        UserAddressesDTO addressesDTO = UserAddressesDTO.builder().district(userAddresses.getDistrict()).city(userAddresses.getCity())
                .defaultAddress(userAddresses.getDefaultAddress())
                .addressName(userAddresses.getAddressName())
                .pinCode(userAddresses.getPinCode())
                .flatNumber(userAddresses.getPinCode())
                .societyName(userAddresses.getSocietyName())
                .societyRegistered(userAddresses.getSocietyRegistered())
                .streetOrArea(userAddresses.getStreetOrArea())
                .wing(userAddresses.getWing()).build();

        return addressesDTO;
    }

    public static UserAddresses userAddressDTOtoUserAddress(UserAddressesDTO userAddressesDTO) {
        UserAddresses userAddresses = UserAddresses.builder().district(userAddressesDTO.getDistrict()).city(userAddressesDTO.getCity())
                .defaultAddress(userAddressesDTO.getDefaultAddress())
                .addressName(userAddressesDTO.getAddressName())
                .pinCode(userAddressesDTO.getPinCode())
                .flatNumber(userAddressesDTO.getPinCode())
                .societyName(userAddressesDTO.getSocietyName())
                .societyRegistered(userAddressesDTO.getSocietyRegistered())
                .streetOrArea(userAddressesDTO.getStreetOrArea())
                .wing(userAddressesDTO.getWing()).build();

        return userAddresses;
    }
}
