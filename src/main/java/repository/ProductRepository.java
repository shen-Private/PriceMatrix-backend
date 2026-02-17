package com.pricematrix.pricematrix.repository;

import com.pricematrix.pricematrix.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

// Interface：定義「這個 Repository 能做什麼」
// JpaRepository 已經幫你實作好 findAll, findById, save, delete...
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 依名稱查詢商品
    java.util.Optional<Product> findByName(String name);
}