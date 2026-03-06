package com.pricematrix.pricematrix.pricing.repository;

import com.pricematrix.pricematrix.pricing.entity.DiscountAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiscountAuditLogRepository extends JpaRepository<DiscountAuditLog, Long> {
    // 查某一筆折扣的所有變更記錄，最新的排前面
    List<DiscountAuditLog> findByDiscountIdOrderByOperatedAtDesc(Long discountId);
}