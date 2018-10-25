package com.mushiny.wms.system.web;

import com.mushiny.wms.system.domain.User;
import com.mushiny.wms.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Laptop-9 on 2018/10/24.
 */
@Component
public class UserController2 {
    private final UserRepository userRepository;
   @Autowired
    public UserController2(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(fixedDelay = 10000L,initialDelay = 20000L)
    public void addUser(){
       User user =new User();
       for( int i=0;i<100;i++){
           List<User> userList=userRepository.findAll();
           System.out.println(userList.size());
           user.setName("admin"+i);
           userRepository.save(user);
       }
    }
}
