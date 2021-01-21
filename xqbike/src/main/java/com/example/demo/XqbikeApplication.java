package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//SpringBoot的入口程序，在Spring程序启动时，会进行扫描，扫描带有特殊注解的类
@SpringBootApplication
public class XqbikeApplication {

	public static void main(String[] args) {
		SpringApplication.run(XqbikeApplication.class, args);
	}

}
