package org.example.priceradar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PriceRadarApplication {

    public static void main(String[] args) {
        SpringApplication.run(PriceRadarApplication.class, args);
    }

}
