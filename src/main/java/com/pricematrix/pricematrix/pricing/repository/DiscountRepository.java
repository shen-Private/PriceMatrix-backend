package com.pricematrix.pricematrix.pricing.repository;

import com.pricematrix.pricematrix.pricing.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
// Interface：定義「這個 Repository 能做什麼」
// JpaRepository 已經幫你實作好 findAll, findById, save, delete...
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    // 不需要寫任何東西，Spring 自動幫你實作
    // 按客戶ID查詢折扣
    List<Discount> findByCustomerId(Long customerId);
    // 按客戶ID + 分類ID查詢折扣
    List<Discount> findByCustomerIdAndProductCategoryId(Long customerId, Long categoryId);
}