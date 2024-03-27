package com.visitorproject.repo;

import com.visitorproject.entity.VisitTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserVisitTrackerRepo extends JpaRepository<VisitTracker, Long> {

    @Query("SELECT v FROM VisitTracker v WHERE v.ownerUsername = ?1 AND v.ownerApproval = false AND v.approvalOrRejectionTime IS NULL")
    List<VisitTracker> findByOwnerUsernameAndOwnerApprovalIsFalseAndRejectionTimeIsNull(String ownerUsername);

    @Query("SELECT v FROM VisitTracker v WHERE v.ownerUsername = ?1 AND v.ownerApproval = true AND v.approvalOrRejectionTime IS NOT NULL")
    List<VisitTracker> findByOwnerUsernameAndOwnerApprovalIsTrueAndRejectionTimeIsNotNull(String ownerUsername);

    List<VisitTracker> findByOwnerUsernameAndVisitorUsername(String ownerUsername, String visitorUsername);

    List<VisitTracker> findByVisitorUsername(String visitorUsername);

}
