package com.pricematrix.pricematrix.sales.repository;

import com.pricematrix.pricematrix.sales.entity.SalesProspect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesProspectRepository extends JpaRepository<SalesProspect, Long> {
}