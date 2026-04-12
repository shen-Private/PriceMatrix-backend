package com.pricematrix.pricematrix.module.sales.repository;

import com.pricematrix.pricematrix.module.sales.entity.SalesProspect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesProspectRepository extends JpaRepository<SalesProspect, Long> {
}