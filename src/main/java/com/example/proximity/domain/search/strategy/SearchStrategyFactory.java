package com.example.proximity.domain.search.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SearchStrategyFactory {

    private final Map<String, SearchStrategy> strategies;

    public SearchStrategy getStrategy(String name) {
        String beanName = switch (name.toLowerCase()) {
            case "sql" -> "sqlSearchStrategy";
            case "grid" -> "gridSearchStrategy";
            case "quad-tree" -> "quadTreeSearchStrategy";
            case "s2" -> "s2SearchStrategy";
            default -> "redisGeoStrategy";
        };

        SearchStrategy strategy = strategies.get(beanName);
        if (strategy == null) {
            return strategies.get("redisGeoStrategy");
        }
        return strategy;
    }
}
