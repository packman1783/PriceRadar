package org.example.priceradar.config;

import org.example.priceradar.model.PriceEntry;
import org.example.priceradar.model.Product;
import org.example.priceradar.model.Store;
import org.example.priceradar.repository.PriceEntryRepository;
import org.example.priceradar.repository.ProductRepository;
import org.example.priceradar.repository.StoreRepository;
import org.example.priceradar.source.PriceSourceRegistry;
import org.example.priceradar.source.PriceSourceResult;
import org.example.priceradar.source.impl.CsvPriceRow;
import org.example.priceradar.source.impl.MockPriceSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * При старте приложения:
 * 1. читает уникальные магазины и товары из CSV (через MockPriceSource),
 *    заводит соответствующие Store/Product в БД, если их ещё нет;
 * 2. для каждой пары (магазин, товар) вызывает PriceSourceRegistry —
 *    то есть тем же путём, каким позже будет работать PriceUpdateScheduler
 *    с реальными Playwright-источниками, — и сохраняет PriceEntry.
 * <p>
 * Это НЕ замена планировщику: в реальном проекте наполнение и обновление
 * цен будет происходить по расписанию (см. будущий PriceUpdateScheduler),
 * а этот класс — только для того, чтобы при первом запуске в БД
 * сразу было с чем работать.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final MockPriceSource mockPriceSource;
    private final PriceSourceRegistry priceSourceRegistry;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final PriceEntryRepository priceEntryRepository;

    public DataInitializer(MockPriceSource mockPriceSource,
                            PriceSourceRegistry priceSourceRegistry,
                            StoreRepository storeRepository,
                            ProductRepository productRepository,
                            PriceEntryRepository priceEntryRepository) {
        this.mockPriceSource = mockPriceSource;
        this.priceSourceRegistry = priceSourceRegistry;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.priceEntryRepository = priceEntryRepository;
    }

    @Override
    public void run(String... args) {
        if (priceEntryRepository.count() > 0) {
            return; // данные уже загружены при предыдущем запуске
        }

        Set<Store> stores = new LinkedHashSet<>();
        Set<String> productNames = new LinkedHashSet<>();

        for (CsvPriceRow row : mockPriceSource.getAllRows()) {
            stores.add(new Store(row.chainName(), row.city(), row.address()));
            productNames.add(row.productName());
        }

        for (Store store : stores) {
            storeRepository.findByChainNameAndCityAndAddress(
                            store.getChainName(), store.getCity(), store.getAddress())
                    .orElseGet(() -> storeRepository.save(store));
        }

        for (String name : productNames) {
            productRepository.findByName(name)
                    .orElseGet(() -> productRepository.save(new Product(name)));
        }

        Instant now = Instant.now();
        for (Store store : storeRepository.findAll()) {
            for (Product product : productRepository.findAll()) {
                Optional<PriceSourceResult> result = priceSourceRegistry
                        .getSourceFor(store)
                        .fetchPrice(product, store);

                result.ifPresent(r -> priceEntryRepository.save(
                        new PriceEntry(product, store, r.price(), r.inStock(), now)));
            }
        }
    }
}
