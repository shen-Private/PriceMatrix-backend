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

    // ===== 單筆入出庫（既有） =====
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

    // ===== 盤點（ADJUST） =====
    @PostMapping("/adjust")
    public ResponseEntity<?> adjustTransaction(
            @RequestBody InventoryTransactionService.AdjustRequest req) {
        try {
            InventoryTransaction tx = transactionService.adjust(req);
            return ResponseEntity.ok(tx);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ===== 批次入庫 =====
    @PostMapping("/batch")
    public ResponseEntity<?> batchIn(
            @RequestBody InventoryTransactionService.BatchInRequest req) {
        try {
            InventoryTransactionService.BatchInResult result = transactionService.batchIn(req);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ===== 最近入庫記錄（盤點 sidebar 用）=====
    @GetMapping("/recent")
    public ResponseEntity<List<InventoryTransaction>> getRecent(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(transactionService.getRecentInbound(days, limit));
    }

    // ===== 查詢特定商品的交易歷史（既有） =====
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<InventoryTransaction>> getByItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(transactionRepository.findByItemIdOrderByOperatedAtDesc(itemId));
    }
}