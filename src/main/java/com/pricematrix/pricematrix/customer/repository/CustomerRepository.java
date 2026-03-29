package com.pricematrix.pricematrix.customer.repository;

import com.pricematrix.pricematrix.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Interface：定義「這個 Repository 能做什麼」
// JpaRepository 已經幫你實作好 findAll, findById, save, delete...
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // 不需要寫任何東西，Spring 自動幫你實作
    // 按名稱模糊搜尋（名稱包含關鍵字就符合）
    List<Customer> findByNameContaining(String name);
    List<Customer> findByActiveTrue();  // 新增


    @Query("SELECT c FROM Customer c WHERE " +
            "LOWER(c.name) LIKE LOWER(:keyword) OR " +
            "LOWER(c.contactPerson) LIKE LOWER(:keyword) OR " +
            "LOWER(c.email) LIKE LOWER(:keyword) OR " +
            "c.phone LIKE :keyword")
    List<Customer> searchByKeyword(@Param("keyword") String keyword);
}