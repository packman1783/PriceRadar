package org.example.priceradar.source;

import java.math.BigDecimal;

/**
 * Результат обращения к источнику цен — единый формат независимо от того,
 * как цена была получена (CSV, парсинг браузера, AI-извлечение).
 */
public record PriceSourceResult(BigDecimal price, boolean inStock) {
}
