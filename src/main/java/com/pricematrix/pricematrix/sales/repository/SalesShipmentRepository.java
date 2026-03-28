package com.pricematrix.pricematrix.sales.repository;

import com.pricematrix.pricematrix.sales.entity.SalesShipment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalesShipmentRepository extends JpaRepository<SalesShipment, Long> {
    List<SalesShipment> findByOrderId(Long orderId);
}