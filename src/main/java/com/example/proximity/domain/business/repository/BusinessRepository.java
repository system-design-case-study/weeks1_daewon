package com.example.proximity.domain.business.repository;

import com.example.proximity.domain.business.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface BusinessRepository extends JpaRepository<Business, UUID> {
    List<Business> findAllByLatitudeBetweenAndLongitudeBetween(
            BigDecimal latMin, BigDecimal latMax, BigDecimal lonMin, BigDecimal lonMax);
}
