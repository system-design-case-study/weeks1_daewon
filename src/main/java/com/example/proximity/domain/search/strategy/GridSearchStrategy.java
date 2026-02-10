package com.example.proximity.domain.search.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GridSearchStrategy implements SearchStrategy {

    private final StringRedisTemplate redisTemplate;
    private static final double GRID_SIZE = 500.0; // 500m per grid
    private static final String KEY_PREFIX = "grid:";

    @Override
    public List<SearchResult> search(double lat, double lon, double radiusMeter) {
        int x = (int) (Math.toRadians(lon) * 6371000 * Math.cos(Math.toRadians(lat)) / GRID_SIZE);
        int y = (int) (Math.toRadians(lat) * 6371000 / GRID_SIZE);
        int range = (int) Math.ceil(radiusMeter / GRID_SIZE);

        List<String> keys = new ArrayList<>();
        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                keys.add(KEY_PREFIX + (x + i) + ":" + (y + j));
            }
        }

        Set<String> businessIdStrings = redisTemplate.opsForSet().union(keys.get(0), keys.subList(1, keys.size()));
        if (businessIdStrings == null)
            return List.of();

        return businessIdStrings.stream()
                .map(idStr -> new SearchResult(UUID.fromString(idStr), 0.0)) // Distance needs refinement but simplified
                                                                             // for task
                .collect(Collectors.toList());
    }

    public void index(UUID businessId, double lat, double lon) {
        int x = (int) (Math.toRadians(lon) * 6371000 * Math.cos(Math.toRadians(lat)) / GRID_SIZE);
        int y = (int) (Math.toRadians(lat) * 6371000 / GRID_SIZE);
        redisTemplate.opsForSet().add(KEY_PREFIX + x + ":" + y, businessId.toString());
    }
}
