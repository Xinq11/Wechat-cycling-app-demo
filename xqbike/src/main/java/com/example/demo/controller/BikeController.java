package com.example.demo.controller;

import com.example.demo.service.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.pojo.Bike;

import java.util.List;

/*
标记这个类是一个用于介绍请求和相应用户的控制器
加@Controller注解后，Spring会对其进行实例化
 */
@Controller

public class BikeController {
	//到Spring容器中查找BikeService类型的实例，然后注入到BikeController实例
	@Autowired
	private BikeService bikeService;

	@RequestMapping("/bike/addBike")
	@ResponseBody
	public String add(@RequestBody Bike bike) {
		//保存到mongodb 调用Service层
		bikeService.save(bike);
		return "hello";
	}

	@RequestMapping("/bike/findNear")
	@ResponseBody
	public List<GeoResult<Bike>> findNear(Double longitude, Double latitude){
		List<GeoResult<Bike>> bikes = bikeService.findNear(longitude,latitude);
		return bikes;
	}
}
