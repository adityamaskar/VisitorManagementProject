package com.visitorproject.repo;

import com.visitorproject.entity.VisitTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVisitTrackerRepo extends JpaRepository<VisitTracker, Long> {

}
