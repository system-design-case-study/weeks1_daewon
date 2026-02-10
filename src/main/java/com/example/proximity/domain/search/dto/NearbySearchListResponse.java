package com.example.proximity.domain.search.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class NearbySearchListResponse {
    private final long total;
    private final List<NearbySearchResponse> businesses;

    public static NearbySearchListResponse of(List<NearbySearchResponse> businesses) {
        return NearbySearchListResponse.builder()
                .total(businesses.size())
                .businesses(businesses)
                .build();
    }
}
