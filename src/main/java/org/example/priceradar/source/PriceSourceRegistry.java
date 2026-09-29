package org.example.priceradar.source;

import org.example.priceradar.model.Store;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Находит подходящий PriceSource для конкретного магазина.
 * Spring сам соберёт сюда все бины, реализующие PriceSource —
 * добавление нового адаптера (например, PyaterochkaPlaywrightSource)
 * не требует правок в этом классе.
 */
@Component
public class PriceSourceRegistry {

    private final List<PriceSource> sources;

    public PriceSourceRegistry(List<PriceSource> sources) {
        this.sources = sources;
    }

    public PriceSource getSourceFor(Store store) {
        return sources.stream()
                .filter(s -> s.supports(store))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Нет источника цен для сети: " + store.getChainName()));
    }
}
