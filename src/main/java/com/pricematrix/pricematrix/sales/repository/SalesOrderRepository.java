package com.pricematrix.pricematrix.sales.repository;

import com.pricematrix.pricematrix.sales.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    List<SalesOrder> findAllByOrderByCreatedAtDesc();
    Optional<SalesOrder> findByQuoteId(Long quoteId);
}