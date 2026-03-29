package com.pricematrix.pricematrix.sales.service;

import com.pricematrix.pricematrix.sales.entity.SalesContactLog;
import com.pricematrix.pricematrix.sales.repository.SalesContactLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalesContactLogService {

    private final SalesContactLogRepository repository;

    public SalesContactLogService(SalesContactLogRepository repository) {
        this.repository = repository;
    }

    // 某客戶的所有聯絡紀錄（時間軸用）
    public List<SalesContactLog> getByCustomerId(Long customerId) {
        return repository.findByCustomerIdOrderByContactedAtDesc(customerId);
    }
    public List<SalesContactLog> getByProspectId(Long prospectId) {
        return repository.findByProspectIdOrderByContactedAtDesc(prospectId);
    }
    // 新增一筆聯絡紀錄
    public SalesContactLog create(SalesContactLog log, HttpServletRequest request) {
        String createdBy = (String) request.getAttribute("username");
        log.setCreatedBy(createdBy);
        log.setCreatedAt(LocalDateTime.now());
        log.setUpdatedAt(LocalDateTime.now());
        return repository.save(log);
    }

    // 修改（只允許改 note / nextAction / result）
    public SalesContactLog update(Long id, SalesContactLog updated) {
        SalesContactLog existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到紀錄：" + id));
        existing.setNote(updated.getNote());
        existing.setNextAction(updated.getNextAction());
        existing.setResult(updated.getResult());
        existing.setUpdatedAt(LocalDateTime.now());
        return repository.save(existing);
    }
}