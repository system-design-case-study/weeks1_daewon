package com.example.proximity.domain.search.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuadTreeSearchStrategy implements SearchStrategy {

    private QuadTreeNode root;

    @PostConstruct
    public void init() {
        // Simple world boundary
        root = new QuadTreeNode(new QuadTreeNode.Boundary(-90, 90, -180, 180));
    }

    @Override
    public List<SearchResult> search(double lat, double lon, double radiusMeter) {
        double latDelta = radiusMeter / 111111.0;
        double lonDelta = radiusMeter / (111111.0 * Math.cos(Math.toRadians(lat)));

        QuadTreeNode.Boundary searchBoundary = new QuadTreeNode.Boundary(
                lat - latDelta, lat + latDelta, lon - lonDelta, lon + lonDelta);

        List<QuadTreeNode.Point> found = new ArrayList<>();
        root.query(searchBoundary, found);

        return found.stream()
                .map(p -> new SearchResult(p.id(), calculateDistance(lat, lon, p.lat(), p.lon())))
                .filter(r -> r.distance() <= radiusMeter)
                .collect(Collectors.toList());
    }

    public void index(QuadTreeNode.Point point) {
        root.insert(point);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
