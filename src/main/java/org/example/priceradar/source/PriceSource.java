package org.example.priceradar.source;

import org.example.priceradar.model.Product;
import org.example.priceradar.model.Store;

import java.util.Optional;

/**
 * Контракт получения цены товара в конкретном магазине.
 * Реализации: MockPriceSource (CSV-фикстуры для разработки/демо),
 * в будущем — PlaywrightPriceSource и наследники для реальных сетей,
 * LlmAssistedPriceSource как резервный вариант.
 * <p>
 * Благодаря этому интерфейсу остальной код (сервис сравнения, планировщик)
 * не знает, откуда берутся цены — реальным скрапингом или фикстурами.
 */
public interface PriceSource {

    /**
     * Название сети, за которую отвечает эта реализация (например, "Пятёрочка").
     * Используется в PriceSourceRegistry для маршрутизации по магазину.
     */
    String getChainName();

    /**
     * Может ли этот источник обслужить данный магазин.
     * По умолчанию — сравнение по названию сети, но реализация может
     * переопределить логику (например, поддерживать несколько сетей сразу).
     */
    default boolean supports(Store store) {
        return getChainName().equalsIgnoreCase(store.getChainName());
    }

    /**
     * Получить текущую цену товара в данном магазине.
     * Возвращает Optional.empty(), если товар не найден в этом магазине вовсе
     * (в отличие от "нет в наличии", когда товар известен, но не продаётся сейчас).
     */
    Optional<PriceSourceResult> fetchPrice(Product product, Store store);
}
