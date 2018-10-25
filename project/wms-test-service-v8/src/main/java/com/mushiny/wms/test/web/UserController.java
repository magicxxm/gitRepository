package com.mushiny.wms.test.web;

import com.mushiny.wms.common.entity.User;
import com.mushiny.wms.common.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Laptop-9 on 2018/10/24.
 */
@Component
public class UserController {
    private final UserRepository userRepository;
    @Autowired
    public UserController(UserRepository userRepository) {
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
