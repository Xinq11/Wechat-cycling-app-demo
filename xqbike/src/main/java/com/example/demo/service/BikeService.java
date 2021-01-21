package com.example.demo.service;

import com.example.demo.pojo.Bike;
import org.springframework.data.geo.GeoResult;

import java.util.List;

public interface BikeService {
    public void save(Bike bike);

    public List<GeoResult<Bike>> findNear(Double longitude, Double latitude);
}
