package com.example.proximity.domain.geo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisGeoService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String GEO_INDEX_KEY = "business:geo:index";

    public void addBusinessLocation(UUID businessId, BigDecimal latitude, BigDecimal longitude) {
        redisTemplate.opsForGeo().add(
                GEO_INDEX_KEY,
                new Point(longitude.doubleValue(), latitude.doubleValue()),
                businessId.toString());
    }

    public void removeBusinessLocation(UUID businessId) {
        redisTemplate.opsForZSet().remove(GEO_INDEX_KEY, businessId.toString());
    }

    public GeoResults<RedisGeoCommands.GeoLocation<Object>> searchNearby(BigDecimal latitude, BigDecimal longitude,
            double radiusInMeters) {
        return redisTemplate.opsForGeo().radius(
                GEO_INDEX_KEY,
                new Circle(new Point(longitude.doubleValue(), latitude.doubleValue()),
                        new Distance(radiusInMeters, RedisGeoCommands.DistanceUnit.METERS)),
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().sortAscending());
    }
}
