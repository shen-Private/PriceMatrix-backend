package com.pricematrix.pricematrix.inventory.controller;

import com.pricematrix.pricematrix.inventory.entity.InventoryItem.StockType;
import java.math.BigDecimal;

public class CreateItemRequest {
    public String productName;
    public BigDecimal basePrice;
    public Long categoryId;
    public Long manufacturerId;

    public StockType stockType;
    public String barcode;
    public String unit;
    public Integer safetyStock;
}