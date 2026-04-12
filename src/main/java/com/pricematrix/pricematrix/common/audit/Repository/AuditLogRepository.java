package com.pricematrix.pricematrix.common.audit.Repository;

import com.pricematrix.pricematrix.common.audit.Entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByEntityTypeAndEntityIdOrderByOperatedAtDesc(
            String entityType, Long entityId
    );
}