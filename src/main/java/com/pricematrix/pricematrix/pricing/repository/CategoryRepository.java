package com.pricematrix.pricematrix.pricing.repository;

import com.pricematrix.pricematrix.pricing.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

// Interface：定義「這個 Repository 能做什麼」
// JpaRepository 已經幫你實作好 findAll, findById, save, delete...
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 不需要寫任何東西，Spring 自動幫你實作
}