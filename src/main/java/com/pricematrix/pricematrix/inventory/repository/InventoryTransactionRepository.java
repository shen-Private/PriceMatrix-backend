package com.pricematrix.pricematrix.inventory.repository;

import com.pricematrix.pricematrix.inventory.entity.InventoryTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    List<InventoryTransaction> findByItemIdOrderByOperatedAtDesc(Long itemId);
    List<InventoryTransaction> findByBatchId(String batchId);

    // 最近N天・上限M筆・IN/ADJUST のみ
    @Query("SELECT t FROM InventoryTransaction t WHERE t.operatedAt >= :since AND t.transactionType IN ('IN', 'ADJUST') ORDER BY t.operatedAt DESC")
    List<InventoryTransaction> findRecentInbound(@Param("since") LocalDateTime since, Pageable pageable);
}