package org.example.priceradar.comparison;

import org.example.priceradar.model.PriceEntry;
import org.example.priceradar.model.Store;
import org.example.priceradar.repository.PriceEntryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Считает, в каком магазине выгоднее купить список товаров целиком.
 * <p>
 * Пока намеренно НЕ учитывает "оптимальное разбиение между несколькими
 * магазинами" (докупить недостающее в другом месте) — это заявлено
 * в архитектуре как отдельная OptimalSplitStrategy для следующего шага,
 * чтобы не смешивать "простое сравнение" и "оптимизацию" в одном методе.
 */
@Service
public class PriceComparisonService {

    private final PriceEntryRepository priceEntryRepository;

    public PriceComparisonService(PriceEntryRepository priceEntryRepository) {
        this.priceEntryRepository = priceEntryRepository;
    }

    public ComparisonResult compare(List<String> requestedProductNames) {
        Set<String> requested = new LinkedHashSet<>(requestedProductNames);

        List<PriceEntry> entries = priceEntryRepository.findByProduct_NameIn(List.copyOf(requested));

        Map<Store, List<PriceEntry>> byStore = entries.stream()
                .filter(PriceEntry::isInStock) // товар не в наличии не считаем "найденным"
                .collect(Collectors.groupingBy(PriceEntry::getStore));

        List<StoreTotal> totals = new ArrayList<>();
        for (Map.Entry<Store, List<PriceEntry>> e : byStore.entrySet()) {
            Store store = e.getKey();
            List<PriceEntry> storeEntries = e.getValue();

            Set<String> found = storeEntries.stream()
                    .map(pe -> pe.getProduct().getName())
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            List<String> missing = requested.stream()
                    .filter(name -> !found.contains(name))
                    .toList();

            BigDecimal total = storeEntries.stream()
                    .map(PriceEntry::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totals.add(new StoreTotal(store, List.copyOf(found), missing, total));
        }

        // сначала магазины, где нашёлся весь список (по возрастанию суммы),
        // затем частично покрывающие список — тоже по возрастанию суммы
        totals.sort(
                Comparator.comparing(StoreTotal::coversFullList).reversed()
                        .thenComparing(StoreTotal::total)
        );

        return new ComparisonResult(totals);
    }
}
