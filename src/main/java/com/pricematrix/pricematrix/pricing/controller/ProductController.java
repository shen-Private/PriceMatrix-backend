package com.pricematrix.pricematrix.pricing.controller;

import com.pricematrix.pricematrix.pricing.entity.Product;
import com.pricematrix.pricematrix.pricing.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:6006"})
public class ProductController {

    private final ProductService ProductService;

    public ProductController(ProductService ProductService) {
        this.ProductService = ProductService;
    }

    // GET /api/products → 取得所有商品（含停用）
    @GetMapping
    public List<Product> getAllProducts() {
        return ProductService.getAllProducts();
    }

    // GET /api/products/active → 取得啟用中的商品
    @GetMapping("/active")
    public List<Product> getActiveProducts() {
        return ProductService.getActiveProducts();
    }

    // GET /api/products/available?customerId=1 → 某客戶還沒有折扣的商品
    @GetMapping("/available")
    public List<Product> getAvailableProducts(@RequestParam Long customerId) {
        return ProductService.getAvailableProductsForCustomer(customerId);
    }

    // POST /api/products → 新增商品
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(ProductService.createProduct(product));
    }

    // PUT /api/products/{id} → 編輯商品
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {
        return ResponseEntity.ok(ProductService.updateProduct(id, product));
    }

    // DELETE /api/products/{id} → 軟刪除（停用）
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateProduct(@PathVariable Long id) {
        ProductService.deactivateProduct(id);
        return ResponseEntity.noContent().build();
    }

    // PUT /api/products/{id}/activate → 恢復啟用
    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateProduct(@PathVariable Long id) {
        ProductService.activateProduct(id);
        return ResponseEntity.noContent().build();
    }
}