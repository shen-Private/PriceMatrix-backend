package com.pricematrix.pricematrix.pricing.controller;

import com.pricematrix.pricematrix.pricing.entity.Customer;
import com.pricematrix.pricematrix.pricing.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:6006"})
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/search")
    public List<Customer> searchCustomers(@RequestParam String name) {
        return customerService.searchCustomersByName(name);
    }

    // POST /api/customers → 新增客戶（admin / sales）
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    // PUT /api/customers/{id} → 更新客戶（admin / sales）
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customer));
    }
    // PATCH /api/customers/{id}/deactivate → 停用客戶（admin only，前端暫不做）
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Customer> deactivateCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.deactivateCustomer(id));
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<Customer> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {
        boolean isActive = body.get("isActive");
        return ResponseEntity.ok(customerService.updateStatus(id, isActive));
    }
    @PostMapping("/convert/{prospectId}")
    public ResponseEntity<Customer> convertProspect(
            @PathVariable Long prospectId) {
        return ResponseEntity.ok(customerService.convertProspect(prospectId));
    }
}