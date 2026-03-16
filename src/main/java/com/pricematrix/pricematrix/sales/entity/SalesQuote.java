package com.pricematrix.pricematrix.sales.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pricematrix.pricematrix.pricing.entity.Customer;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "sales_quote")
@Data
@NoArgsConstructor
public class SalesQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "quote", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<SalesQuoteItem> items = new ArrayList<>();

    @Column(nullable = false)
    private String status = "DRAFT";

    private String note;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_quote_id")
    @JsonBackReference
    private SalesQuote parentQuote;
}