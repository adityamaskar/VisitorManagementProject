package com.visitorproject.repo;

import com.visitorproject.entity.UserAddresses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAdressesRepo extends JpaRepository<UserAddresses, Integer> {
}
