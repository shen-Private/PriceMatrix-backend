package com.pricematrix.pricematrix.audit.Repository;

import com.pricematrix.pricematrix.audit.Entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByEntityTypeAndEntityIdOrderByOperatedAtDesc(
            String entityType, Long entityId
    );
}