package com.visitorproject.repo;

import com.visitorproject.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile, Long> {
    public UserProfile findByUserName(String userName);

    public UserProfile findByemail(String email);

    public UserProfile findByPhoneNum(String phoneNum);

}
