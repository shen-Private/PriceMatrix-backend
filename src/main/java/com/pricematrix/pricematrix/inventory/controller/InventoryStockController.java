package com.pricematrix.pricematrix.inventory.controller;

import com.pricematrix.pricematrix.inventory.entity.InventoryStock;
import com.pricematrix.pricematrix.inventory.service.InventoryStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory/stock")
@RequiredArgsConstructor
public class InventoryStockController {

    private final InventoryStockService stockService;

    @GetMapping("/{itemId}")
    public InventoryStock getStock(@PathVariable Long itemId) {
        return stockService.getStockByItemId(itemId);
    }

    @PostMapping("/{itemId}/in")
    public InventoryStock recordIn(
            @PathVariable Long itemId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String batchId,
            @RequestParam(required = false) String note,
            @RequestParam String operatedBy
    ) {
        return stockService.recordIn(itemId, quantity, batchId, note, operatedBy);
    }

    @PostMapping("/{itemId}/out")
    public InventoryStock recordOut(
            @PathVariable Long itemId,
            @RequestParam Integer quantity,
            @RequestParam(required = false) String note,
            @RequestParam String operatedBy
    ) {
        return stockService.recordOut(itemId, quantity, note, operatedBy);
    }
}