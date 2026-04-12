package com.pricematrix.pricematrix.module.sales.repository;

import com.pricematrix.pricematrix.module.sales.entity.SalesQuoteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesQuoteItemRepository extends JpaRepository<SalesQuoteItem, Long> {
    List<SalesQuoteItem> findByQuoteId(Long quoteId);
}