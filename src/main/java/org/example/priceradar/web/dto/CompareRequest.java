package org.example.priceradar.web.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CompareRequest(
        @NotEmpty List<String> products
) {
}
