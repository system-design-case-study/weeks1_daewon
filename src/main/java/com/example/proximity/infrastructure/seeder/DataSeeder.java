package com.example.proximity.infrastructure.seeder;

import com.example.proximity.domain.business.dto.BusinessRequest;
import com.example.proximity.domain.business.service.BusinessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Slf4j
@Component
@Profile("local")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final BusinessService businessService;
    private final Random random = new Random();

    private static final int TARGET_COUNT = 10000;

    // Gangnam Station, Seoul
    private static final double BASE_LAT = 37.4979;
    private static final double BASE_LON = 127.0276;

    // Approx 10km range in degrees
    private static final double RANGE = 0.1;

    @Override
    public void run(String... args) {
        log.info("Starting data seeding for {} businesses...", TARGET_COUNT);

        long start = System.currentTimeMillis();
        for (int i = 1; i <= TARGET_COUNT; i++) {
            double latOffset = (random.nextDouble() - 0.5) * RANGE;
            double lonOffset = (random.nextDouble() - 0.5) * RANGE;

            BigDecimal lat = BigDecimal.valueOf(BASE_LAT + latOffset).setScale(6, RoundingMode.HALF_UP);
            BigDecimal lon = BigDecimal.valueOf(BASE_LON + lonOffset).setScale(6, RoundingMode.HALF_UP);

            BusinessRequest request = BusinessRequest.builder()
                    .name("Dummy Business " + i)
                    .description("Auto-generated business for performance testing " + i)
                    .address("Mock Address " + i + ", Seoul, Korea")
                    .latitude(lat)
                    .longitude(lon)
                    .build();

            try {
                businessService.createBusiness(request);
                if (i % 1000 == 0) {
                    log.info("Seeded {}/{} businesses...", i, TARGET_COUNT);
                }
            } catch (Exception e) {
                log.error("Failed to seed business {}: {}", i, e.getMessage());
            }
        }
        long end = System.currentTimeMillis();

        log.info("Data seeding completed in {} ms.", (end - start));
    }
}
