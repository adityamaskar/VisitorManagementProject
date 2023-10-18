package com.visitorproject.repo;

import com.visitorproject.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepo extends JpaRepository<UserProfile, Integer> {
    public UserProfile findByUserName(String userName);

    public UserProfile findByemail(String email);

    public UserProfile findByPhoneNum(Integer phoneNum);
}
