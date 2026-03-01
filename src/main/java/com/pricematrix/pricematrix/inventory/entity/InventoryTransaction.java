package com.pricematrix.pricematrix.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transaction")
@Data
public class InventoryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private InventoryItem item;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "quantity_before", nullable = false)
    private Integer quantityBefore;

    @Column(name = "quantity_after", nullable = false)
    private Integer quantityAfter;

    @Column(name = "batch_id", length = 30)
    private String batchId;

    @Column(length = 500)
    private String note;

    @Column(name = "operated_by", nullable = false, length = 100)
    private String operatedBy;

    @Column(name = "operated_at", nullable = false)
    private LocalDateTime operatedAt;

    @PrePersist
    protected void onCreate() {
        operatedAt = LocalDateTime.now();
    }

    public enum TransactionType {
        IN, OUT, ADJUST
    }
}