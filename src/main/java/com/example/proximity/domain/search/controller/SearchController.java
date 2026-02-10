package com.example.proximity.domain.search.controller;

import com.example.proximity.common.response.ApiResponse;
import com.example.proximity.domain.search.dto.NearbySearchListResponse;
import com.example.proximity.domain.search.dto.NearbySearchRequest;
import com.example.proximity.domain.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Nearby Search", description = "Endpoints for searching businesses by location")
@RestController
@RequestMapping("/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Search nearby businesses")
    @GetMapping("/nearby")
    public ApiResponse<NearbySearchListResponse> searchNearby(@Valid NearbySearchRequest request) {
        return ApiResponse.success(NearbySearchListResponse.of(searchService.searchNearby(request)));
    }
}
