package com.pricematrix.pricematrix.sales.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuoteItemRequest {
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice; // 前端帶入，初期值由前端算好
}