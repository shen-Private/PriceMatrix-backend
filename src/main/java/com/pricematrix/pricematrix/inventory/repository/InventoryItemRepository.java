package com.pricematrix.pricematrix.inventory.repository;

import com.pricematrix.pricematrix.inventory.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findByIsActiveTrue();
    List<InventoryItem> findByStockTypeAndIsActiveTrue(InventoryItem.StockType stockType);
    java.util.Optional<InventoryItem>   findByBarcode(String barcode);
}