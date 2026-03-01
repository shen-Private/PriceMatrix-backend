package com.pricematrix.pricematrix.pricing.service;

import com.pricematrix.pricematrix.pricing.entity.Product;
import com.pricematrix.pricematrix.pricing.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository ProductRepository;

    public ProductService(ProductRepository ProductRepository) {
        this.ProductRepository = ProductRepository;
    }

    // 取得所有商品（含停用，管理頁面用）
    public List<Product> getAllProducts() {
        return ProductRepository.findAll();
    }

    // 取得啟用中的商品（一般用途）
    public List<Product> getActiveProducts() {
        return ProductRepository.findByIsActiveTrue();
    }

    // 取得某客戶還沒有折扣的商品（新增折扣下拉選單用）
    public List<Product> getAvailableProductsForCustomer(Long customerId) {
        return ProductRepository.findAvailableProductsForCustomer(customerId);
    }

    // 新增商品
    public Product createProduct(Product product) {
        product.setActive(true);
        return ProductRepository.save(product);
    }

    // 編輯商品（名稱、定價、分類）
    public Product updateProduct(Long id, Product updated) {
        Product existing = ProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到商品：" + id));
        existing.setName(updated.getName());
        existing.setBasePrice(updated.getBasePrice());
        existing.setCategory(updated.getCategory());
        return ProductRepository.save(existing);
    }

    // 軟刪除（停用商品）
    public void deactivateProduct(Long id) {
        Product existing = ProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到商品：" + id));
        existing.setActive(false);
        ProductRepository.save(existing);
    }

    // 恢復啟用
    public void activateProduct(Long id) {
        Product existing = ProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到商品：" + id));
        existing.setActive(true);
        ProductRepository.save(existing);
    }
}