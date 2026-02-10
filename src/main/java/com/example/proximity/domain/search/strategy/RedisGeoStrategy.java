package com.example.proximity.domain.search.strategy;

import com.example.proximity.domain.geo.service.RedisGeoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RedisGeoStrategy implements SearchStrategy {

    private final RedisGeoService redisGeoService;

    @Override
    public List<SearchResult> search(double lat, double lon, double radiusMeter) {
        var results = redisGeoService.searchNearby(BigDecimal.valueOf(lat), BigDecimal.valueOf(lon), radiusMeter);
        if (results == null)
            return List.of();

        return results.getContent().stream()
                .map(res -> new SearchResult(UUID.fromString(res.getContent().getName().toString()),
                        res.getDistance().getValue()))
                .collect(Collectors.toList());
    }
}
