package com.pricematrix.pricematrix.inventory.service;

import com.pricematrix.pricematrix.inventory.entity.InventoryItem;
import com.pricematrix.pricematrix.inventory.entity.InventoryStock;
import com.pricematrix.pricematrix.inventory.entity.OutsourceInquiryLog;
import com.pricematrix.pricematrix.inventory.repository.InventoryItemRepository;
import com.pricematrix.pricematrix.inventory.repository.InventoryStockRepository;
import com.pricematrix.pricematrix.inventory.repository.OutsourceInquiryLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryItemService {

    private final InventoryItemRepository itemRepository;
    private final InventoryStockRepository inventoryStockRepository;
    private final OutsourceInquiryLogRepository outsourceInquiryLogRepository;

    public List<InventoryItem> getAllActiveItems() {
        return itemRepository.findByIsActiveTrue();
    }

    public InventoryItem getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found: " + id));
    }
    public java.util.Optional<InventoryItem> getItemByBarcode(String barcode) {
        return itemRepository.findByBarcode(barcode);
    }
    public InventoryItem createItem(InventoryItem item) {
        return itemRepository.save(item);
    }

    public InventoryItem updateItem(Long id, InventoryItem updated) {
        InventoryItem item = getItemById(id);
        item.setStockType(updated.getStockType());
        item.setUnit(updated.getUnit());
        item.setSafetyStock(updated.getSafetyStock());
        return itemRepository.save(item);
    }

    public void deactivateItem(Long id) {
        InventoryItem item = getItemById(id);
        item.setIsActive(false);
        itemRepository.save(item);
    }

    // 回傳給前端的 DTO
    public record ItemOverviewDTO(
            InventoryItem item,
            InventoryStock stock,
            OutsourceInquiryLog latestInquiry
    ) {}

    public List<ItemOverviewDTO> getOverview() {
        List<InventoryItem> items = itemRepository.findByIsActiveTrue();
        return items.stream().map(item -> {
            InventoryStock stock = inventoryStockRepository.findByItemId(item.getId()).orElse(null);
            OutsourceInquiryLog latestInquiry = outsourceInquiryLogRepository
                    .findTopByItemIdOrderByConfirmedAtDesc(item.getId())
                    .orElse(null);
            return new ItemOverviewDTO(item, stock, latestInquiry);
        }).toList();
    }
}