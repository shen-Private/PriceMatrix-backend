package com.pricematrix.pricematrix.pricing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pricing_discount_audit_logs")
public class DiscountAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 記錄哪一筆折扣被改
    @Column(nullable = false)
    private Long discountId;

    // 改之前的折扣率
    private BigDecimal oldRatio;

    // 改之後的折扣率
    @Column(nullable = false)
    private BigDecimal newRatio;

    // 操作類型：UPDATE / BATCH_UPDATE / CREATE / DELETE
    @Column(nullable = false)
    private String action;

    // 誰改的（目前先寫死，之後接登入系統）
    private String operatedBy;

    @Column(nullable = false)
    private LocalDateTime operatedAt = LocalDateTime.now();

    public DiscountAuditLog() {}

    // Getters & Setters
    public Long getId() { return id; }
    public Long getDiscountId() { return discountId; }
    public void setDiscountId(Long discountId) { this.discountId = discountId; }
    public BigDecimal getOldRatio() { return oldRatio; }
    public void setOldRatio(BigDecimal oldRatio) { this.oldRatio = oldRatio; }
    public BigDecimal getNewRatio() { return newRatio; }
    public void setNewRatio(BigDecimal newRatio) { this.newRatio = newRatio; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getOperatedBy() { return operatedBy; }
    public void setOperatedBy(String operatedBy) { this.operatedBy = operatedBy; }
    public LocalDateTime getOperatedAt() { return operatedAt; }
    public void setOperatedAt(LocalDateTime operatedAt) { this.operatedAt = operatedAt; }
}