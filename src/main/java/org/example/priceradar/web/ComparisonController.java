package org.example.priceradar.web;

import jakarta.validation.Valid;

import org.example.priceradar.comparison.ComparisonResult;
import org.example.priceradar.comparison.PriceComparisonService;
import org.example.priceradar.web.dto.CompareRequest;
import org.example.priceradar.web.dto.CompareResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ComparisonController {

    private final PriceComparisonService priceComparisonService;

    public ComparisonController(PriceComparisonService priceComparisonService) {
        this.priceComparisonService = priceComparisonService;
    }

    /**
     * Принимает список названий товаров и возвращает сравнение по магазинам,
     * отсортированное по возрастанию суммы (магазины с полным покрытием списка — первые).
     * <p>
     * Пример запроса:
     * POST /api/compare
     * { "products": ["Молоко 1л", "Хлеб белый", "Сахар 1кг"] }
     */
    @PostMapping("/compare")
    public CompareResponse compare(@Valid @RequestBody CompareRequest request) {
        ComparisonResult result = priceComparisonService.compare(request.products());
        return CompareResponse.from(result);
    }
}
