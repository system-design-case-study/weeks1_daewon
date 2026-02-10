package com.example.proximity.domain.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class NearbySearchRequest {

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    @Schema(description = "Latitude", example = "37.4979")
    private BigDecimal lat;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Schema(description = "Longitude", example = "127.0276")
    private BigDecimal lon;

    @Min(1)
    @Max(20000)
    @Schema(description = "Search radius in meters", example = "5000")
    private Integer radius = 5000;

    @Schema(description = "Search strategy", example = "redis-geo", allowableValues = { "redis-geo", "sql", "grid",
            "quad-tree", "s2" })
    private String strategy = "redis-geo";
}
