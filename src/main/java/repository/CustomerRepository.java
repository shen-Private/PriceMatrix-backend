package com.pricematrix.pricematrix.repository;

import com.pricematrix.pricematrix.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Interface：定義「這個 Repository 能做什麼」
// JpaRepository 已經幫你實作好 findAll, findById, save, delete...
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // 不需要寫任何東西，Spring 自動幫你實作
    // 按名稱模糊搜尋（名稱包含關鍵字就符合）
    List<Customer> findByNameContaining(String name);
}