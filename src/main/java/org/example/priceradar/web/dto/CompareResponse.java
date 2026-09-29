package org.example.priceradar.web.dto;

import org.example.priceradar.comparison.ComparisonResult;

import java.util.List;

public record CompareResponse(List<StoreTotalResponse> stores) {

    public static CompareResponse from(ComparisonResult result) {
        List<StoreTotalResponse> stores = result.storeTotals().stream()
                .map(StoreTotalResponse::from)
                .toList();
        return new CompareResponse(stores);
    }
}
