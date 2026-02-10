package com.example.proximity.domain.search.strategy;

import com.example.proximity.domain.business.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SqlSearchStrategy implements SearchStrategy {

    private final BusinessRepository businessRepository;

    @Override
    public List<SearchResult> search(double lat, double lon, double radiusMeter) {
        // Simple approximation: 1 degree latitude ~= 111,111 meters
        double latDegreeDelta = radiusMeter / 111111.0;
        double lonDegreeDelta = radiusMeter / (111111.0 * Math.cos(Math.toRadians(lat)));

        BigDecimal latMin = BigDecimal.valueOf(lat - latDegreeDelta);
        BigDecimal latMax = BigDecimal.valueOf(lat + latDegreeDelta);
        BigDecimal lonMin = BigDecimal.valueOf(lon - lonDegreeDelta);
        BigDecimal lonMax = BigDecimal.valueOf(lon + lonDegreeDelta);

        return businessRepository.findAllByLatitudeBetweenAndLongitudeBetween(latMin, latMax, lonMin, lonMax)
                .stream()
                .map(b -> new SearchResult(b.getId(),
                        calculateDistance(lat, lon, b.getLatitude().doubleValue(), b.getLongitude().doubleValue())))
                .filter(r -> r.distance() <= radiusMeter)
                .collect(Collectors.toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000; // meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
