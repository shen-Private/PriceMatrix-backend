package com.pricematrix.pricematrix.sales.repository;


import com.pricematrix.pricematrix.sales.entity.SalesQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
@Repository
public interface SalesQuoteRepository extends JpaRepository<SalesQuote, Long> {
    List<SalesQuote> findByCustomerId(Long customerId);
    @Query("SELECT q FROM SalesQuote q WHERE CAST(q.id AS string) LIKE :keyword")
    List<SalesQuote> searchByKeyword(@Param("keyword") String keyword);
}