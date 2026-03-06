package com.pricematrix.pricematrix.pricing.controller;

import com.pricematrix.pricematrix.pricing.entity.Discount;
import com.pricematrix.pricematrix.pricing.entity.DiscountAuditLog;
import com.pricematrix.pricematrix.pricing.service.DiscountService;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/discounts")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:6006"})
public class DiscountController {

    private final DiscountService DiscountService;

    public DiscountController(DiscountService DiscountService) {
        this.DiscountService = DiscountService;
    }

    @GetMapping
    public List<Discount> getAllDiscounts() {
        return DiscountService.getAllDiscounts();
    }

    @GetMapping("/customer/{customerId}")
    public List<Discount> getDiscountsByCustomer(
            @PathVariable Long customerId,
            @RequestParam(required = false) Long categoryId) {
        return DiscountService.getDiscountsByCustomerId(customerId, categoryId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Discount> updateDiscount(
            @PathVariable Long id,
            @RequestBody Discount updatedDiscount) {
        return ResponseEntity.ok(DiscountService.updateDiscount(id, updatedDiscount));
    }

    // 批次修改：body = { "1": 0.85, "2": 0.90, "5": 0.75 }
    @PutMapping("/batch")
    public ResponseEntity<Void> batchUpdateDiscounts(
            @RequestBody Map<Long, BigDecimal> updates) {
        DiscountService.batchUpdateDiscounts(updates);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        DiscountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Discount> createDiscount(@RequestBody Discount discount) {
        Discount created = DiscountService.createDiscount(discount);
        return ResponseEntity.ok(created);
    }

    // 查某筆折扣的變更歷史
    @GetMapping("/{id}/audit-logs")
    public ResponseEntity<List<DiscountAuditLog>> getAuditLogs(@PathVariable Long id) {
        return ResponseEntity.ok(DiscountService.getAuditLogs(id));
    }
}