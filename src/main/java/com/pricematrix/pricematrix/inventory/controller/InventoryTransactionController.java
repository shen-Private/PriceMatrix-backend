package com.pricematrix.pricematrix.inventory.controller;

import com.pricematrix.pricematrix.inventory.entity.InventoryTransaction;
import com.pricematrix.pricematrix.inventory.service.InventoryTransactionService;
import com.pricematrix.pricematrix.inventory.repository.InventoryTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/transactions")
@RequiredArgsConstructor
public class InventoryTransactionController {

    private final InventoryTransactionService transactionService;
    private final InventoryTransactionRepository transactionRepository;

    @PostMapping
    public ResponseEntity<?> createTransaction(
            @RequestBody InventoryTransactionService.TransactionRequest req) {
        try {
            InventoryTransaction tx = transactionService.process(req);
            return ResponseEntity.ok(tx);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<InventoryTransaction>> getByItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(transactionRepository.findByItemIdOrderByOperatedAtDesc(itemId));
    }
}