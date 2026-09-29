package org.example.priceradar.comparison;

import org.example.priceradar.model.Store;

import java.math.BigDecimal;
import java.util.List;

/**
 * Результат подсчёта корзины в одном конкретном магазине.
 *
 * @param foundProducts    товары из списка, которые нашлись в этом магазине (и есть в наличии)
 * @param missingProducts  товары из списка, которых в этом магазине нет вовсе или нет в наличии
 * @param total            сумма по найденным товарам
 */
public record StoreTotal(
        Store store,
        List<String> foundProducts,
        List<String> missingProducts,
        BigDecimal total
) {
    public boolean coversFullList() {
        return missingProducts.isEmpty();
    }
}
