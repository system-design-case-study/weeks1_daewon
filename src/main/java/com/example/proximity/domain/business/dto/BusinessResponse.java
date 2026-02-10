package com.example.proximity.domain.business.dto;

import com.example.proximity.domain.business.entity.Business;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class BusinessResponse {
    private final UUID id;
    private final String name;
    private final String description;
    private final String address;
    private final BigDecimal latitude;
    private final BigDecimal longitude;

    public static BusinessResponse from(Business business) {
        return BusinessResponse.builder()
                .id(business.getId())
                .name(business.getName())
                .description(business.getDescription())
                .address(business.getAddress())
                .latitude(business.getLatitude())
                .longitude(business.getLongitude())
                .build();
    }
}
