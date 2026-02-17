package com.pricematrix.pricematrix.controller;

import com.pricematrix.pricematrix.entity.Customer;
import com.pricematrix.pricematrix.service.CustomerService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController          // 告訴 Spring：這是一個 API Controller
@RequestMapping("/customers")  // 這個 Controller 處理 /customers 開頭的請求
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:6006"})
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // GET /customers → 取得所有客戶
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }
    // GET /customers/search?name=張三 → 按名稱搜尋客戶
    @GetMapping("/search")
    public List<Customer> searchCustomers(@RequestParam String name) {
        return customerService.searchCustomersByName(name);
    }
}