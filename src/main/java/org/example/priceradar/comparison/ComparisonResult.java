package org.example.priceradar.comparison;

import java.util.List;

/**
 * Результат сравнения списка покупок по всем известным магазинам,
 * отсортированный по возрастанию суммы (магазины, полностью покрывающие
 * список, идут раньше частично покрывающих — см. PriceComparisonService).
 */
public record ComparisonResult(List<StoreTotal> storeTotals) {

    public StoreTotal cheapestFullMatch() {
        return storeTotals.stream()
                .filter(StoreTotal::coversFullList)
                .findFirst()
                .orElse(null);
    }
}
