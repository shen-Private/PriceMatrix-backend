package com.pricematrix.pricematrix.sales.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pricematrix.pricematrix.pricing.entity.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "sales_quote_item")
@Data
@NoArgsConstructor
public class SalesQuoteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quote_id", nullable = false)
    @JsonBackReference
    private SalesQuote quote;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;
}