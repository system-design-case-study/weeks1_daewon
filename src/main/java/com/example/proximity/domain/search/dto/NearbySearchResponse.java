package com.example.proximity.domain.search.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class NearbySearchResponse {
    private final UUID id;
    private final String name;
    private final String address;
    private final double distance;
    private final String distanceUnit;

    public static NearbySearchResponse of(UUID id, String name, String address, double distance, String unit) {
        return NearbySearchResponse.builder()
                .id(id)
                .name(name)
                .address(address)
                .distance(distance)
                .distanceUnit(unit)
                .build();
    }
}
