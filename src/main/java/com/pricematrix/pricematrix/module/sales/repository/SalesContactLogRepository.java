package com.pricematrix.pricematrix.module.sales.repository;

import com.pricematrix.pricematrix.module.sales.entity.SalesContactLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalesContactLogRepository extends JpaRepository<SalesContactLog, Long> {
    List<SalesContactLog> findByCustomerIdOrderByContactedAtDesc(Long customerId);
    List<SalesContactLog> findByProspectIdOrderByContactedAtDesc(Long prospectId);
}