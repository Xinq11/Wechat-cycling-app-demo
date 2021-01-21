package com.example.demo.service;

import com.example.demo.pojo.Bike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BikeServiceImpl implements BikeService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Bike bike) {
        //调用具体的业务
        mongoTemplate.insert(bike,"bikes");
    }

    /*
    根据当前经纬度查找附近单车
     */
    @Override
    public List<GeoResult<Bike>> findNear(Double longitude, Double latitude) {
        NearQuery nearQuery = NearQuery.near(longitude,latitude);
        //查找的范围和距离单位 200m
        nearQuery.maxDistance(0.2, Metrics.KILOMETERS);
        GeoResults<Bike> geoResults = mongoTemplate.geoNear(
                nearQuery.query(new Query(Criteria.where("status").is(0)).limit(200)),Bike.class);
        return geoResults.getContent();
    }
}
