package org.example.priceradar.source.impl;

import org.example.priceradar.model.Product;
import org.example.priceradar.model.Store;
import org.example.priceradar.source.PriceSource;
import org.example.priceradar.source.PriceSourceResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.math.BigDecimal;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * "Источник" цен для разработки и демонстрации: вместо реального похода
 * в интернет читает заранее подготовленный CSV-файл.
 * <p>
 * Специально реализует PriceSource — на его место позже встанут
 * PlaywrightPriceSource-наследники без изменения остального кода
 * (сервиса сравнения, планировщика, реестра источников).
 * <p>
 * supports(Store) переопределён: этот источник, в отличие от специализированных
 * будущих адаптеров, обслуживает сразу все сети из CSV.
 */
@Component
public class MockPriceSource implements PriceSource {

    private final List<CsvPriceRow> rows;

    public MockPriceSource(@Value("${priceradar.mock-data-file}") String mockDataFile) {
        this.rows = loadRows(mockDataFile);
    }

    @Override
    public String getChainName() {
        return "mock"; // условное имя, реальная маршрутизация — через supports()
    }

    @Override
    public boolean supports(Store store) {
        return rows.stream().anyMatch(r -> r.chainName().equalsIgnoreCase(store.getChainName()));
    }

    @Override
    public Optional<PriceSourceResult> fetchPrice(Product product, Store store) {
        return rows.stream()
                .filter(r -> r.chainName().equalsIgnoreCase(store.getChainName())
                        && r.city().equalsIgnoreCase(store.getCity())
                        && r.address().equalsIgnoreCase(store.getAddress())
                        && r.productName().equalsIgnoreCase(product.getName()))
                .findFirst()
                .map(r -> new PriceSourceResult(r.price(), r.inStock()));
    }

    /**
     * Все строки CSV — нужны DataInitializer'у, чтобы понять,
     * какие Store и Product вообще завести в базе при первом запуске.
     */
    public List<CsvPriceRow> getAllRows() {
        return Collections.unmodifiableList(rows);
    }

    private List<CsvPriceRow> loadRows(String classpathLocation) {
        List<CsvPriceRow> result = new ArrayList<>();
        Resource resource = new PathMatchingResourcePatternResolver()
                .getResource("classpath:" + classpathLocation);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            String line = reader.readLine(); // пропускаем заголовок
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] cols = line.split(",", -1);
                result.add(new CsvPriceRow(
                        cols[0].trim(),
                        cols[1].trim(),
                        cols[2].trim(),
                        cols[3].trim(),
                        new BigDecimal(cols[4].trim()),
                        Boolean.parseBoolean(cols[5].trim())
                ));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать фикстуры цен: " + classpathLocation, e);
        }
        return result;
    }
}
