package com.example.proximity.domain.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessRequest {

    @NotBlank
    @Size(min = 2, max = 100)
    @Schema(description = "Business Name", example = "Starbucks Gangnam")
    private String name;

    @Schema(description = "Business Description", example = "Best coffee in town")
    private String description;

    @NotBlank
    @Size(min = 10, max = 255)
    @Schema(description = "Business Address", example = "123 Gangnam-daero, Seoul")
    private String address;

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    @Schema(description = "Latitude", example = "37.4979")
    private BigDecimal latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    @Schema(description = "Longitude", example = "127.0276")
    private BigDecimal longitude;
}
