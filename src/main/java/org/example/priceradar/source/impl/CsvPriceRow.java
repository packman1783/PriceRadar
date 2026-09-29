package org.example.priceradar.source.impl;

import java.math.BigDecimal;

/**
 * Одна строка CSV-фикстуры цен. Не путать с сущностями JPA —
 * это "сырые" данные из источника до того, как они превратились
 * в Store/Product/PriceEntry.
 */
public record CsvPriceRow(
        String chainName,
        String city,
        String address,
        String productName,
        BigDecimal price,
        boolean inStock
) {
}
