package com.example.proximity.domain.business.controller;

import com.example.proximity.common.response.ApiResponse;
import com.example.proximity.domain.business.dto.BusinessRequest;
import com.example.proximity.domain.business.dto.BusinessResponse;
import com.example.proximity.domain.business.service.BusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Business Management", description = "Endpoints for managing business entities")
@RestController
@RequestMapping("/v1/businesses")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @Operation(summary = "Create a new business")
    @PostMapping
    public ApiResponse<BusinessResponse> createBusiness(@Valid @RequestBody BusinessRequest request) {
        return ApiResponse.success(businessService.createBusiness(request));
    }

    @Operation(summary = "Get business details")
    @GetMapping("/{id}")
    public ApiResponse<BusinessResponse> getBusiness(@PathVariable UUID id) {
        return ApiResponse.success(businessService.getBusiness(id));
    }

    @Operation(summary = "Update business details")
    @PutMapping("/{id}")
    public ApiResponse<BusinessResponse> updateBusiness(
            @PathVariable UUID id,
            @Valid @RequestBody BusinessRequest request) {
        return ApiResponse.success(businessService.updateBusiness(id, request));
    }

    @Operation(summary = "Delete a business")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBusiness(@PathVariable UUID id) {
        businessService.deleteBusiness(id);
        return ApiResponse.success(null);
    }
}
