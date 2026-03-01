package com.pricematrix.pricematrix.pricing.repository;

import com.pricematrix.pricematrix.pricing.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 依名稱查詢商品
    Optional<Product> findByName(String name);

    // 取得所有啟用中的商品（商品主檔管理用）
    List<Product> findByIsActiveTrue();

    // 取得某客戶還沒有折扣紀錄的商品（新增折扣下拉選單用）
    @Query("""
        SELECT p FROM Product p
        WHERE p.isActive = true
        AND p.id NOT IN (
            SELECT d.product.id FROM Discount d WHERE d.customer.id = :customerId
        )
    """)
    List<Product> findAvailableProductsForCustomer(@Param("customerId") Long customerId);
}