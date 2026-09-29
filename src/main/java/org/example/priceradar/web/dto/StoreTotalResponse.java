package org.example.priceradar.web.dto;

import org.example.priceradar.comparison.StoreTotal;

import java.math.BigDecimal;
import java.util.List;

/**
 * Наружу отдаём не сущности JPA напрямую (Store), а плоский DTO —
 * это защищает от случайной утечки внутренней структуры БД в API
 * и от проблем с ленивой загрузкой при сериализации в JSON.
 */
public record StoreTotalResponse(
        String chainName,
        String city,
        String address,
        BigDecimal total,
        List<String> foundProducts,
        List<String> missingProducts,
        boolean coversFullList
) {
    public static StoreTotalResponse from(StoreTotal storeTotal) {
        return new StoreTotalResponse(
                storeTotal.store().getChainName(),
                storeTotal.store().getCity(),
                storeTotal.store().getAddress(),
                storeTotal.total(),
                storeTotal.foundProducts(),
                storeTotal.missingProducts(),
                storeTotal.coversFullList()
        );
    }
}
