package com.pricematrix.pricematrix.inventory.repository;

import com.pricematrix.pricematrix.inventory.entity.InventoryStock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventoryStockRepository extends JpaRepository<InventoryStock, Long> {
    Optional<InventoryStock> findByItemId(Long itemId);

}