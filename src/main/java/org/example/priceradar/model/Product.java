package org.example.priceradar.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Товар из списка покупок пользователя. Название — как оно фигурирует
 * и в фикстурах/парсерах, и в запросе пользователя, поэтому оно уникально
 * и используется как ключ сопоставления.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // "Молоко 1л", "Хлеб белый"

    public Product(String name) {
        this.name = name;
    }
}
