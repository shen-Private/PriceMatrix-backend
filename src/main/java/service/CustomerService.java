package com.pricematrix.pricematrix.service;

import com.pricematrix.pricematrix.entity.Customer;
import com.pricematrix.pricematrix.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service  // 告訴 Spring：這是一個 Service 元件
public class CustomerService {

    // 注入 Repository（Spring 自動幫你 new）
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // 取得所有客戶
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    // 按名稱模糊搜尋客戶
    public List<Customer> searchCustomersByName(String name) {
        return customerRepository.findByNameContaining(name);
    }

}