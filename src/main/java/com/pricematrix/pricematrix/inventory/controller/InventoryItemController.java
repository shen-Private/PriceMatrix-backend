package com.pricematrix.pricematrix.inventory.controller;

import com.pricematrix.pricematrix.inventory.entity.InventoryItem;
import com.pricematrix.pricematrix.inventory.service.InventoryItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory/items")
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService itemService;

    @GetMapping
    public List<InventoryItem> getAllItems() {
        return itemService.getAllActiveItems();
    }

    @GetMapping("/{id}")
    public InventoryItem getItem(@PathVariable Long id) {
        return itemService.getItemById(id);
    }

    @PostMapping
    public InventoryItem createItem(@RequestBody InventoryItem item) {
        return itemService.createItem(item);
    }

    @PutMapping("/{id}")
    public InventoryItem updateItem(@PathVariable Long id, @RequestBody InventoryItem item) {
        return itemService.updateItem(id, item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateItem(@PathVariable Long id) {
        itemService.deactivateItem(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<InventoryItem> getItemByBarcode(@PathVariable String barcode) {
        return itemService.getItemByBarcode(barcode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/overview")
    public List<InventoryItemService.ItemOverviewDTO> getOverview() {
        return itemService.getOverview();
    }
}