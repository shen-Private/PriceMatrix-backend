package com.pricematrix.pricematrix.controller;

import com.pricematrix.pricematrix.entity.Discount;
import com.pricematrix.pricematrix.service.DiscountService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController          // 告訴 Spring：這是一個 API Controller
@RequestMapping("/api/discounts")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:6006"})
public class DiscountController {

    private final DiscountService DiscountService;

    public DiscountController(DiscountService DiscountService) {
        this.DiscountService = DiscountService;
    }

    // GET /Discounts → 取得所有客戶
    @GetMapping
    public List<Discount> getAllDiscounts() {
        return DiscountService.getAllDiscounts();
    }
    // GET /discounts/customer/{customerId}?categoryId=2 → 可選分類篩選
    @GetMapping("/customer/{customerId}")
    public List<Discount> getDiscountsByCustomer(
            @PathVariable Long customerId,
            @RequestParam(required = false) Long categoryId) {  // required=false = 不帶也可以
        return DiscountService.getDiscountsByCustomerId(customerId, categoryId);
    }
    // PUT /discounts/{id} — 修改折扣率
    @PutMapping("/{id}")
    public ResponseEntity<Discount> updateDiscount(
            @PathVariable Long id,
            @RequestBody Discount updatedDiscount) {
        return ResponseEntity.ok(DiscountService.updateDiscount(id, updatedDiscount));    }
    // 刪除折扣記錄的 API 端點
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        DiscountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }
    // 新增折扣記錄的 API 端點
    @PostMapping
    public ResponseEntity<Discount> createDiscount(@RequestBody Discount discount) {
        Discount created = DiscountService.createDiscount(discount);
        return ResponseEntity.ok(created);
    }
}