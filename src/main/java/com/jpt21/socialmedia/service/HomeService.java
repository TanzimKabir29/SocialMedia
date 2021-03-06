package com.jpt21.socialmedia.service;

import com.jpt21.socialmedia.model.Status;
import com.jpt21.socialmedia.model.UserAccount;
import com.jpt21.socialmedia.repository.StatusRepository;
import com.jpt21.socialmedia.repository.UserAccountRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class HomeService {
    
    private final StatusRepository statusRepository;
    private final UserAccountRepository userAccountRepository;

    public List<Status> getStatusList() {
        return statusRepository.findByIsPublic(true);
    }

    public Boolean saveStatus(Status status){
        status.setCreationTime(new Date(System.currentTimeMillis()));
        if(statusRepository.save(status) ==null){
            return false;
        }
        log.info("Status saved for user: {}", status.getPosterName());
        return true;
    }

    public String registerUser(UserAccount userAccount) {
        log.info("Received request to register user {}", userAccount);
        //Check if a user with same username already exists
        if(userAccountRepository.findByUserName(userAccount.getUserName()).isPresent()){
            return "Username exists already";
        }
        //Check if a user with same email already exists
        if(userAccountRepository.findByEmail(userAccount.getEmail()).isPresent()){
            return "Email exists already";
        }
        //Hardcode isActive and Role because we aren't using them explicitly
        userAccount.setIsActive(true);
        userAccount.setRoles("ROLE_USER");
        if(userAccountRepository.save(userAccount) == null){
            return "Unknown error creating user";
        }
        log.info("User created with username: {}", userAccount.getUserName());
        return "User created successfully";
    }
}
