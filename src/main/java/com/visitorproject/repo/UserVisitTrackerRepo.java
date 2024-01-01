package com.visitorproject.repo;

import com.visitorproject.entity.VisitTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserVisitTrackerRepo extends JpaRepository<VisitTracker, Long> {
//    @Query("select v from VisitTracker v where upper(v.ownerUsername) like upper(?1) and v.ownerApproval = false")
//    VisitTracker findByOwnerUsernameLikeIgnoreCaseAndOwnerApprovalFalse(String ownerUsername);

    List<VisitTracker> findByOwnerUsernameAndOwnerApprovalIsFalse(String ownerUsername);

    List<VisitTracker> findByOwnerUsernameAndOwnerApprovalIsTrue(String ownerUsername);


}
