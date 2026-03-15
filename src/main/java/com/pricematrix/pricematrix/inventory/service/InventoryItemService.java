package com.pricematrix.pricematrix.inventory.service;

import com.pricematrix.pricematrix.inventory.controller.CreateItemRequest;
import com.pricematrix.pricematrix.inventory.entity.InventoryItem;
import com.pricematrix.pricematrix.inventory.entity.InventoryStock;
import com.pricematrix.pricematrix.inventory.entity.Manufacturer;
import com.pricematrix.pricematrix.inventory.entity.OutsourceInquiryLog;
import com.pricematrix.pricematrix.inventory.repository.InventoryItemRepository;
import com.pricematrix.pricematrix.inventory.repository.InventoryStockRepository;
import com.pricematrix.pricematrix.pricing.repository.ManufacturerRepository;
import com.pricematrix.pricematrix.inventory.repository.OutsourceInquiryLogRepository;
import com.pricematrix.pricematrix.pricing.entity.Category;
import com.pricematrix.pricematrix.pricing.entity.Product;
import com.pricematrix.pricematrix.pricing.repository.CategoryRepository;
import com.pricematrix.pricematrix.pricing.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryItemService {

    private final InventoryItemRepository itemRepository;
    private final InventoryStockRepository inventoryStockRepository;
    private final OutsourceInquiryLogRepository outsourceInquiryLogRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;

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

    @Transactional
    public InventoryItem createItemWithProduct(CreateItemRequest req) {
        Product product = new Product();
        product.setName(req.productName);
        product.setBasePrice(req.basePrice);

        Category category = categoryRepository.findById(req.categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        if (req.manufacturerId != null) {
            Manufacturer manufacturer = manufacturerRepository.findById(req.manufacturerId)
                    .orElseThrow(() -> new RuntimeException("Manufacturer not found"));
            product.setManufacturer(manufacturer);
        }

        Product savedProduct = productRepository.save(product);

        InventoryItem item = new InventoryItem();
        item.setProduct(savedProduct);
        item.setStockType(req.stockType);
        item.setBarcode(req.barcode);
        item.setUnit(req.unit);
        item.setSafetyStock(req.safetyStock);
        item.setIsActive(true);
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());

        InventoryItem savedItem = itemRepository.save(item);

// internal / outsource_warehouse 才建 stock
        if (req.stockType == InventoryItem.StockType.internal ||
                req.stockType == InventoryItem.StockType.outsource_warehouse) {
            InventoryStock stock = new InventoryStock();
            stock.setItem(savedItem);
            stock.setQuantity(0);
            stock.setLastUpdatedAt(LocalDateTime.now());
            inventoryStockRepository.save(stock);
        }


        return savedItem;  // ← 改這行，不要再 save 一次
    }

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