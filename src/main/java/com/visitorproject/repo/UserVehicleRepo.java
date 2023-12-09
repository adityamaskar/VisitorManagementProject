package com.visitorproject.repo;

import com.visitorproject.entity.Vehicles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVehicleRepo extends JpaRepository<Vehicles, Long> {
}
