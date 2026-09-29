package org.example.priceradar.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Цена конкретного товара в конкретном магазине, зафиксированная в момент сбора.
 * Хранение collectedAt важно: цены "протухают", и в будущем можно будет
 * брать только последнюю запись по каждой паре (товар, магазин).
 */
@Entity
@Table(name = "price_entries")
@Getter
@Setter
@NoArgsConstructor
public class PriceEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean inStock;

    @Column(nullable = false)
    private Instant collectedAt;

    public PriceEntry(Product product, Store store, BigDecimal price, boolean inStock, Instant collectedAt) {
        this.product = product;
        this.store = store;
        this.price = price;
        this.inStock = inStock;
        this.collectedAt = collectedAt;
    }
}
