package com.pricematrix.pricematrix.inventory.service;

import com.pricematrix.pricematrix.inventory.entity.InventoryItem;
import com.pricematrix.pricematrix.inventory.entity.InventoryStock;
import com.pricematrix.pricematrix.inventory.entity.InventoryTransaction;
import com.pricematrix.pricematrix.inventory.repository.InventoryItemRepository;
import com.pricematrix.pricematrix.inventory.repository.InventoryStockRepository;
import com.pricematrix.pricematrix.inventory.repository.InventoryTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryTransactionService {

    private final InventoryTransactionRepository transactionRepository;
    private final InventoryItemRepository itemRepository;
    private final InventoryStockRepository stockRepository;

    public record TransactionRequest(
            Long itemId,
            String transactionType, // "IN" or "OUT"
            Integer quantity,
            String operatedBy,
            String note
    ) {}

    @Transactional
    public InventoryTransaction process(TransactionRequest req) {
        // 找商品
        InventoryItem item = itemRepository.findById(req.itemId())
                .orElseThrow(() -> new RuntimeException("Item not found: " + req.itemId()));

        // 找目前庫存
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

        // 更新庫存
        stock.setQuantity(after);
        stockRepository.save(stock);

        // 記錄交易
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
}