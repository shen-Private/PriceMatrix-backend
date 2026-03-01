package com.pricematrix.pricematrix.inventory.repository;

import com.pricematrix.pricematrix.inventory.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    List<InventoryTransaction> findByItemIdOrderByOperatedAtDesc(Long itemId);
    List<InventoryTransaction> findByBatchId(String batchId);
}