package com.pricematrix.pricematrix.inventory.service;

import com.pricematrix.pricematrix.inventory.entity.InventoryStock;
import com.pricematrix.pricematrix.inventory.entity.InventoryTransaction;
import com.pricematrix.pricematrix.inventory.repository.InventoryStockRepository;
import com.pricematrix.pricematrix.inventory.repository.InventoryTransactionRepository;
import com.pricematrix.pricematrix.inventory.entity.InventoryItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryStockService {

    private final InventoryStockRepository stockRepository;
    private final InventoryTransactionRepository transactionRepository;
    private final InventoryItemService itemService;

    public InventoryStock getStockByItemId(Long itemId) {
        return stockRepository.findByItemId(itemId)
                .orElseThrow(() -> new RuntimeException("Stock not found for item: " + itemId));
    }

    @Transactional
    public InventoryStock recordIn(Long itemId, Integer quantity, String batchId, String note, String operatedBy) {
        InventoryItem item = itemService.getItemById(itemId);
        InventoryStock stock = stockRepository.findByItemId(itemId)
                .orElseGet(() -> {
                    InventoryStock s = new InventoryStock();
                    s.setItem(item);
                    s.setQuantity(0);
                    return s;
                });

        int before = stock.getQuantity();
        int after = before + quantity;
        stock.setQuantity(after);
        stockRepository.save(stock);

        InventoryTransaction tx = new InventoryTransaction();
        tx.setItem(item);
        tx.setTransactionType(InventoryTransaction.TransactionType.IN);
        tx.setQuantity(quantity);
        tx.setQuantityBefore(before);
        tx.setQuantityAfter(after);
        tx.setBatchId(batchId);
        tx.setNote(note);
        tx.setOperatedBy(operatedBy);
        transactionRepository.save(tx);

        return stock;
    }

    @Transactional
    public InventoryStock recordOut(Long itemId, Integer quantity, String note, String operatedBy) {
        InventoryItem item = itemService.getItemById(itemId);
        InventoryStock stock = getStockByItemId(itemId);

        if (stock.getQuantity() < quantity) {
            throw new RuntimeException("庫存不足");
        }

        int before = stock.getQuantity();
        int after = before - quantity;
        stock.setQuantity(after);
        stockRepository.save(stock);

        InventoryTransaction tx = new InventoryTransaction();
        tx.setItem(item);
        tx.setTransactionType(InventoryTransaction.TransactionType.OUT);
        tx.setQuantity(quantity);
        tx.setQuantityBefore(before);
        tx.setQuantityAfter(after);
        tx.setNote(note);
        tx.setOperatedBy(operatedBy);
        transactionRepository.save(tx);

        return stock;
    }

}