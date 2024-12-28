package com.aditya.visitorproject.service;

import com.aditya.visitorproject.dtos.AuthCompleteEvent;
import com.aditya.visitorproject.entity.UserProfile;
import com.aditya.visitorproject.repo.UserProfileRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    private UserProfileRepo userProfileRepo;

    @Autowired
    private KafkaTemplate<String, AuthCompleteEvent> kafkaTemplate;

    @Async
    public void sendNotificationOnAuth(String userName){
        UserProfile userByUserName = userProfileRepo.findByUserName(userName);
        try {
            kafkaTemplate.send(kafkaTemplate.getDefaultTopic(),
                    new AuthCompleteEvent(userName, userByUserName.getFirstName() + " " + userByUserName.getLastName()));
        }catch (Exception e){
            log.error("Can't sent notification, some error occurred : " + e.getMessage());
            log.error("Error : " + e);
        }
    }
}
