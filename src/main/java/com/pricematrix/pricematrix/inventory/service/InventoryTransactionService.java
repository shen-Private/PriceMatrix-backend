package com.pricematrix.pricematrix.inventory.service;

import com.pricematrix.pricematrix.inventory.entity.InventoryItem;
import com.pricematrix.pricematrix.inventory.entity.InventoryStock;
import com.pricematrix.pricematrix.inventory.entity.InventoryTransaction;
import com.pricematrix.pricematrix.inventory.repository.InventoryItemRepository;
import com.pricematrix.pricematrix.inventory.repository.InventoryStockRepository;
import com.pricematrix.pricematrix.inventory.repository.InventoryTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryTransactionService {

    private final InventoryTransactionRepository transactionRepository;
    private final InventoryItemRepository itemRepository;
    private final InventoryStockRepository stockRepository;

    // ===== 單筆請求（IN / OUT） =====
    public record TransactionRequest(
            Long itemId,
            String transactionType, // "IN" or "OUT"
            Integer quantity,
            String operatedBy,
            String note
    ) {}

    // ===== 盤點請求（ADJUST）：傳入實際數量，系統計算差異 =====
    public record AdjustRequest(
            Long itemId,
            Integer actualQuantity, // 盤點後的實際數量
            String operatedBy,
            String note
    ) {}

    // ===== 批次入庫請求：多筆商品一起入庫 =====
    public record BatchItemRequest(
            Long itemId,
            Integer quantity,
            String note
    ) {}

    public record BatchInRequest(
            List<BatchItemRequest> items,
            String operatedBy
    ) {}

    // ===== 批次入庫回傳 =====
    public record BatchInResult(
            String batchId,
            List<InventoryTransaction> transactions
    ) {}

    // ===== 單筆入出庫 =====
    @Transactional
    public InventoryTransaction process(TransactionRequest req) {
        InventoryItem item = itemRepository.findById(req.itemId())
                .orElseThrow(() -> new RuntimeException("Item not found: " + req.itemId()));

        InventoryStock stock = stockRepository.findByItemId(req.itemId())
                .orElseThrow(() -> new RuntimeException("Stock not found for item: " + req.itemId()));

        int before = stock.getQuantity();
        int after;

        if (req.transactionType().equals("IN")) {
            after = before + req.quantity();
        } else if (req.transactionType().equals("OUT")) {
            if (before < req.quantity()) {
                throw new RuntimeException("庫存不足：現有 " + before + "，出庫 " + req.quantity());
            }
            after = before - req.quantity();
        } else {
            throw new RuntimeException("Invalid transaction type: " + req.transactionType());
        }

        stock.setQuantity(after);
        stockRepository.save(stock);

        InventoryTransaction tx = new InventoryTransaction();
        tx.setItem(item);
        tx.setTransactionType(InventoryTransaction.TransactionType.valueOf(req.transactionType()));
        tx.setQuantity(req.quantity());
        tx.setQuantityBefore(before);
        tx.setQuantityAfter(after);
        tx.setOperatedBy(req.operatedBy());
        tx.setNote(req.note());

        return transactionRepository.save(tx);
    }

    // ===== 盤點（ADJUST）：傳入實際數量，系統算出差異並記錄 =====
    @Transactional
    public InventoryTransaction adjust(AdjustRequest req) {
        InventoryItem item = itemRepository.findById(req.itemId())
                .orElseThrow(() -> new RuntimeException("Item not found: " + req.itemId()));

        InventoryStock stock = stockRepository.findByItemId(req.itemId())
                .orElseThrow(() -> new RuntimeException("Stock not found for item: " + req.itemId()));

        int before = stock.getQuantity();
        int after = req.actualQuantity();
        // 差異：正數 = 盤盈，負數 = 盤虧
        int diff = after - before;

        stock.setQuantity(after);
        stockRepository.save(stock);

        InventoryTransaction tx = new InventoryTransaction();
        tx.setItem(item);
        tx.setTransactionType(InventoryTransaction.TransactionType.ADJUST);
        tx.setQuantity(diff); // 差異值（可為負）
        tx.setQuantityBefore(before);
        tx.setQuantityAfter(after);
        tx.setOperatedBy(req.operatedBy());
        tx.setNote(req.note());

        return transactionRepository.save(tx);
    }

    // ===== 最近入庫記錄（盤點 sidebar 用）=====
    public List<InventoryTransaction> getRecentInbound(int days, int limit) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return transactionRepository.findRecentInbound(since, PageRequest.of(0, limit));
    }

    // ===== 批次入庫：多筆商品同一次入庫，共用同一個 batchId =====
    @Transactional
    public BatchInResult batchIn(BatchInRequest req) {
        // 產生 batchId：BATCH-YYYYMMDD-時間戳後4碼
        String batchId = "BATCH-"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-"
                + String.format("%04d", System.currentTimeMillis() % 10000);

        List<InventoryTransaction> results = req.items().stream().map(entry -> {
            InventoryItem item = itemRepository.findById(entry.itemId())
                    .orElseThrow(() -> new RuntimeException("Item not found: " + entry.itemId()));

            InventoryStock stock = stockRepository.findByItemId(entry.itemId())
                    .orElseThrow(() -> new RuntimeException("Stock not found for item: " + entry.itemId()));

            int before = stock.getQuantity();
            int after = before + entry.quantity();

            stock.setQuantity(after);
            stockRepository.save(stock);

            InventoryTransaction tx = new InventoryTransaction();
            tx.setItem(item);
            tx.setTransactionType(InventoryTransaction.TransactionType.IN);
            tx.setQuantity(entry.quantity());
            tx.setQuantityBefore(before);
            tx.setQuantityAfter(after);
            tx.setBatchId(batchId);
            tx.setOperatedBy(req.operatedBy());
            tx.setNote(entry.note());

            return transactionRepository.save(tx);
        }).toList();

        return new BatchInResult(batchId, results);
    }
}