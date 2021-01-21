package com.example.demo.service;

import com.aliyuncs.exceptions.ClientException;
import com.example.demo.pojo.User;
import org.springframework.data.mongodb.core.MongoTemplate;

public interface UserService {


    void register(User user);

    boolean sendMsg(String countryCode, String phoneNum) throws ClientException;

    void update(User user);

    boolean verify(String phoneNum, String verifyCode);


}
