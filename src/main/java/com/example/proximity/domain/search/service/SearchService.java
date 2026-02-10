package com.example.proximity.domain.search.service;

import com.example.proximity.domain.business.entity.Business;
import com.example.proximity.domain.business.repository.BusinessRepository;
import com.example.proximity.domain.search.dto.NearbySearchRequest;
import com.example.proximity.domain.search.dto.NearbySearchResponse;
import com.example.proximity.domain.search.strategy.SearchStrategy;
import com.example.proximity.domain.search.strategy.SearchStrategyFactory;
import com.example.proximity.infrastructure.redis.RedisCacheService;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final SearchStrategyFactory strategyFactory;
    private final RedisCacheService redisCacheService;
    private final BusinessRepository businessRepository;

    private static final String BUSINESS_CACHE_PREFIX = "business:detail:";

    public List<NearbySearchResponse> searchNearby(NearbySearchRequest request) {
        // 1. Get strategy and search for IDs
        SearchStrategy strategy = strategyFactory.getStrategy(request.getStrategy());
        List<SearchStrategy.SearchResult> searchResults = strategy.search(
                request.getLat().doubleValue(),
                request.getLon().doubleValue(),
                request.getRadius().doubleValue());

        if (searchResults.isEmpty()) {
            return List.of();
        }

        // 2. Extract UUIDs and prepare cache keys
        List<UUID> businessIds = searchResults.stream()
                .map(SearchStrategy.SearchResult::businessId)
                .toList();

        List<String> cacheKeys = businessIds.stream()
                .map(id -> BUSINESS_CACHE_PREFIX + id)
                .collect(Collectors.toList());

        // 3. Multi-Get from Redis (Layer 2: Detail Cache)
        List<Object> cachedBusinesses = redisCacheService.multiGet(cacheKeys);
        Map<UUID, Business> businessMap = new HashMap<>();
        List<UUID> missingIds = new ArrayList<>();

        for (int i = 0; i < businessIds.size(); i++) {
            UUID id = businessIds.get(i);
            Object cached = cachedBusinesses.get(i);
            if (cached instanceof Business) {
                businessMap.put(id, (Business) cached);
            } else {
                missingIds.add(id);
            }
        }

        // 4. Fetch missing from DB and update cache (Cache-aside)
        if (!missingIds.isEmpty()) {
            log.debug("Cache miss for IDs: {}. Fetching from DB", missingIds);
            List<Business> fromDb = businessRepository.findAllById(missingIds);
            for (Business business : fromDb) {
                businessMap.put(business.getId(), business);
                redisCacheService.set(BUSINESS_CACHE_PREFIX + business.getId(), business, 1, TimeUnit.DAYS);
            }
        }

        // 5. Combine results
        Map<UUID, Double> distanceMap = searchResults.stream()
                .collect(Collectors.toMap(SearchStrategy.SearchResult::businessId,
                        SearchStrategy.SearchResult::distance, (v1, v2) -> v1));

        return businessIds.stream()
                .map(id -> {
                    Business business = businessMap.get(id);
                    if (business == null)
                        return null;

                    return NearbySearchResponse.of(
                            id,
                            business.getName(),
                            business.getAddress(),
                            distanceMap.getOrDefault(id, 0.0),
                            org.springframework.data.geo.Metrics.NEUTRAL.toString());
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
    }
}
