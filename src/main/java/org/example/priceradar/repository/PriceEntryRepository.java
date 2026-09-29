package org.example.priceradar.repository;

import org.example.priceradar.model.PriceEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceEntryRepository extends JpaRepository<PriceEntry, Long> {

    /**
     * Все записи о ценах для товаров с данными названиями.
     * Дальнейшая группировка по магазину и выбор последней цены —
     * в сервисе сравнения (comparison), чтобы не размазывать бизнес-логику по слоям.
     */
    List<PriceEntry> findByProduct_NameIn(List<String> productNames);
}
