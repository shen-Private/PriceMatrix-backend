package com.pricematrix.pricematrix.module.pricing.controller;

import com.pricematrix.pricematrix.common.audit.Entity.AuditLog;
import com.pricematrix.pricematrix.module.pricing.entity.Discount;
import com.pricematrix.pricematrix.module.pricing.service.DiscountService;
import jakarta.servlet.http.HttpServletRequest;
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


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        DiscountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }

    // 查某筆折扣的變更歷史
    @GetMapping("/{id}/audit-logs")
    public ResponseEntity<List<AuditLog>> getAuditLogs(@PathVariable Long id) {
        return ResponseEntity.ok(DiscountService.getAuditLogs(id));
    }
    @GetMapping("/customer/{customerId}/product/{productId}")
    public ResponseEntity<Discount> getDiscountByCustomerAndProduct(
            @PathVariable Long customerId,
            @PathVariable Long productId) {
        return DiscountService.findByCustomerAndProduct(customerId, productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Discount> updateDiscount(
            @PathVariable Long id,
            @RequestBody Discount updatedDiscount,
            HttpServletRequest request) {
        return ResponseEntity.ok(DiscountService.updateDiscount(id, updatedDiscount, request));
    }
    @PutMapping("/batch")
    public ResponseEntity<Void> batchUpdateDiscounts(
            @RequestBody Map<Long, BigDecimal> updates,
            HttpServletRequest request) {
        DiscountService.batchUpdateDiscounts(updates, request);
        return ResponseEntity.ok().build();
    }
    @PostMapping
    public ResponseEntity<Discount> createDiscount(
            @RequestBody Discount discount,
            HttpServletRequest request) {
        Discount created = DiscountService.createDiscount(discount, request);
        return ResponseEntity.ok(created);
    }
}