package com.aditya.visitorproject.repo;

import com.aditya.visitorproject.entity.UserAddresses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAdressesRepo extends JpaRepository<UserAddresses, Long> {
}
