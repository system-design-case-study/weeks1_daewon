package com.example.proximity.domain.search.strategy;

import com.google.common.geometry.S1Angle;
import com.google.common.geometry.S2Cap;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2CellUnion;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2RegionCoverer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S2SearchStrategy implements SearchStrategy {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String S2_INDEX_PREFIX = "s2:index:";
    private static final int S2_LEVEL = 13; // Approx 1.27km^2 cells

    @Override
    public List<SearchResult> search(double lat, double lon, double radiusMeter) {
        S2LatLng latLng = S2LatLng.fromDegrees(lat, lon);
        double angleDegrees = (radiusMeter / 111000.0);
        S2Cap cap = S2Cap.fromAxisAngle(latLng.toPoint(), S1Angle.degrees(angleDegrees));

        S2RegionCoverer coverer = S2RegionCoverer.builder()
                .setMinLevel(S2_LEVEL)
                .setMaxLevel(S2_LEVEL)
                .build();
        S2CellUnion cellUnion = coverer.getCovering(cap);

        List<SearchResult> results = new ArrayList<>();
        for (S2CellId cellId : cellUnion) {
            String key = S2_INDEX_PREFIX + cellId.toToken();
            Set<Object> members = redisTemplate.opsForSet().members(key);
            if (members != null) {
                for (Object member : members) {
                    results.add(new SearchResult(UUID.fromString(member.toString()), 0.0));
                }
            }
        }
        return results;
    }

    public void index(UUID businessId, double lat, double lon) {
        S2CellId cellId = S2CellId.fromLatLng(S2LatLng.fromDegrees(lat, lon)).parent(S2_LEVEL);
        String key = S2_INDEX_PREFIX + cellId.toToken();
        redisTemplate.opsForSet().add(key, businessId.toString());
    }
}
