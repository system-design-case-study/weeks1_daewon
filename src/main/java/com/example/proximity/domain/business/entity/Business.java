package com.example.proximity.domain.business.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "business")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "business_id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank
    @Size(min = 10, max = 255)
    @Column(nullable = false, length = 255)
    private String address;

    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal longitude;

    @Builder
    public Business(String name, String description, String address, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void update(String name, String description, String address, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
