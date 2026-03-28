package com.pricematrix.pricematrix.pricing.service;

import com.pricematrix.pricematrix.pricing.entity.Customer;
import com.pricematrix.pricematrix.pricing.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();  // 改這行，移除 findByActiveTrue()
    }

    // 新增停用方法
    public Customer deactivateCustomer(Long id) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + id));
        c.setActive(false);
        return customerRepository.save(c);
    }

    public List<Customer> searchCustomersByName(String name) {
        return customerRepository.findByNameContaining(name);
    }

    // 新增客戶
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // 更新客戶
    public Customer updateCustomer(Long id, Customer updated) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + id));

        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        existing.setContactPerson(updated.getContactPerson());
        existing.setNote(updated.getNote());
        existing.setParent(updated.getParent());

        return customerRepository.save(existing);
    }
    public Customer updateStatus(Long id, boolean isActive) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setActive(isActive);
        return customerRepository.save(customer);
    }
}