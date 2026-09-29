package org.example.priceradar.repository;

import org.example.priceradar.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByChainNameAndCityAndAddress(String chainName, String city, String address);
}
