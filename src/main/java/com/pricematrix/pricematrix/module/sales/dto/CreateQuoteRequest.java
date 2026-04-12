package com.pricematrix.pricematrix.module.sales.dto;


import lombok.Data;
import java.util.List;

@Data
public class CreateQuoteRequest {
    private Long customerId;
    private String note;
    private List<QuoteItemRequest> items;
}