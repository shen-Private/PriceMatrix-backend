package com.pricematrix.pricematrix.pricing.service;

import com.pricematrix.pricematrix.pricing.entity.Category;
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
    public Product updateProduct(Long id, String name, java.math.BigDecimal basePrice, Long categoryId) {
        Product existing = ProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到商品：" + id));
        if (name != null) existing.setName(name);
        if (basePrice != null) existing.setBasePrice(basePrice);
        if (categoryId != null) {
            Category category = new Category();
            category.setId(categoryId);
            existing.setCategory(category);
        }
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

    // 簡易建檔（倉庫掃到未知條碼用）
    public Product createPendingProduct(String name, Long manufacturerId) {
        Product product = new Product();
        product.setName(name);
        product.setBasePrice(java.math.BigDecimal.ZERO);
        product.setActive(true);
        product.setStatus("pending");
        if (manufacturerId != null) {
            com.pricematrix.pricematrix.inventory.entity.Manufacturer m =
                    new com.pricematrix.pricematrix.inventory.entity.Manufacturer();
            m.setId(manufacturerId);
            product.setManufacturer(m);
        }
        return ProductRepository.save(product);
    }

    // 取得待確認商品列表（CS管理用）
    public List<Product> getPendingProducts() {
        return ProductRepository.findByStatus("pending");
    }

    // CS確認商品
    public Product confirmProduct(Long id) {
        Product existing = ProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到商品：" + id));
        existing.setStatus("active");
        return ProductRepository.save(existing);
    }
}