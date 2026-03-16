package com.pricematrix.pricematrix.sales.repository;

import com.pricematrix.pricematrix.sales.entity.SalesQuoteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesQuoteItemRepository extends JpaRepository<SalesQuoteItem, Long> {
    List<SalesQuoteItem> findByQuoteId(Long quoteId);
}