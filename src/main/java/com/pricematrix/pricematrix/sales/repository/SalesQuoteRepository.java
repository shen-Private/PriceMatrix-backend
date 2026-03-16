package com.pricematrix.pricematrix.sales.repository;


import com.pricematrix.pricematrix.sales.entity.SalesQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesQuoteRepository extends JpaRepository<SalesQuote, Long> {
    List<SalesQuote> findByCustomerId(Long customerId);
}