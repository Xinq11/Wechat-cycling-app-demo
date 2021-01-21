package com.example.demo.controller;

import com.aliyuncs.exceptions.ClientException;
import com.example.demo.pojo.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/user/genCode")
    @ResponseBody
    public boolean genVerifyCode(String countryCode,String phoneNum) {
        boolean flag = true;
        try {
            userService.sendMsg(countryCode,phoneNum);
        } catch (ClientException e) {
            e.printStackTrace();
            flag = false;
        }

        return flag;
    }

    @RequestMapping("/user/verify")
    @ResponseBody
    public boolean verify(@RequestBody String phoneNum,String verifyCode){
        return userService.verify(phoneNum,verifyCode);
    }

    @RequestMapping("/user/register")
    @ResponseBody
    public boolean reg(@RequestBody User user){//接收json类型的参数，然后set到对应实体类中的属性
        boolean flag = true;
        try {
            userService.register(user);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @RequestMapping("/user/deposit")
    @ResponseBody
    public boolean deposit(@RequestBody User user){
        boolean flag = true;
        try {
            userService.update(user);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @RequestMapping("user/identify")
    @ResponseBody
    public boolean indentify(@RequestBody User user){
        boolean flag = true;
        try {
            userService.update(user);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
}
