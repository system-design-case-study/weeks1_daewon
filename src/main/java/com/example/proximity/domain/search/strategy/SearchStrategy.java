package com.example.proximity.domain.search.strategy;

import java.util.List;
import java.util.UUID;

public interface SearchStrategy {
    List<SearchResult> search(double lat, double lon, double radiusMeter);

    record SearchResult(UUID businessId, double distance) {
    }
}
