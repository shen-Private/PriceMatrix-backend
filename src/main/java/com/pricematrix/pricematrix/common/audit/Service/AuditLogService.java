package com.pricematrix.pricematrix.common.audit.Service;

import com.pricematrix.pricematrix.common.audit.Entity.AuditLog;
import com.pricematrix.pricematrix.common.audit.Repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    // 單筆欄位變更
    public void log(String entityType, Long entityId,
                    String fieldName, String oldValue, String newValue,
                    String operatedBy) {
        AuditLog log = AuditLog.builder()
                .entityType(entityType)
                .entityId(entityId)
                .fieldName(fieldName)
                .oldValue(oldValue)
                .newValue(newValue)
                .operatedBy(operatedBy)
                .operatedAt(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }

    // 狀態流轉專用（field_name 固定為 "status"）
    public void logStatus(String entityType, Long entityId,
                          String oldStatus, String newStatus,
                          String operatedBy) {
        log(entityType, entityId, "status", oldStatus, newStatus, operatedBy);
    }

    // 查詢某筆資料的所有 log
    public List<AuditLog> getLogs(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByOperatedAtDesc(
                entityType, entityId
        );
    }
}