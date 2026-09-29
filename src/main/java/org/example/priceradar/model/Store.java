package org.example.priceradar.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Конкретная точка продаж определённой сети (например, "Пятёрочка" на ул. Ленина, 1).
 * Разделение "сеть / конкретный адрес" нужно, потому что цены могут отличаться
 * даже внутри одной сети в разных городах.
 */
@Entity
@Table(name = "stores", uniqueConstraints = @UniqueConstraint(columnNames = {"chainName", "city", "address"}))
@Getter
@Setter
@NoArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String chainName; // "Пятёрочка", "Магнит", "Чижик"

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    public Store(String chainName, String city, String address) {
        this.chainName = chainName;
        this.city = city;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Store store)) return false;
        return Objects.equals(chainName, store.chainName)
                && Objects.equals(city, store.city)
                && Objects.equals(address, store.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chainName, city, address);
    }
}
