package com.pricematrix.pricematrix.sales.controller;

import com.pricematrix.pricematrix.sales.entity.SalesContactLog;
import com.pricematrix.pricematrix.sales.service.SalesContactLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/contact-logs")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:6006"})
public class SalesContactLogController {

    private final SalesContactLogService service;

    public SalesContactLogController(SalesContactLogService service) {
        this.service = service;
    }

    // 某客戶的所有聯絡紀錄
    @GetMapping("/customer/{customerId}")
    public List<SalesContactLog> getByCustomer(@PathVariable Long customerId) {
        return service.getByCustomerId(customerId);
    }
    @GetMapping("/prospect/{prospectId}")
    public List<SalesContactLog> getByProspect(@PathVariable Long prospectId) {
        return service.getByProspectId(prospectId);
    }
    // 新增聯絡紀錄
    @PostMapping
    public ResponseEntity<SalesContactLog> create(
            @RequestBody SalesContactLog log,
            HttpServletRequest request) {
        return ResponseEntity.ok(service.create(log, request));
    }

    // 修改聯絡紀錄
    @PutMapping("/{id}")
    public ResponseEntity<SalesContactLog> update(
            @PathVariable Long id,
            @RequestBody SalesContactLog updated) {
        return ResponseEntity.ok(service.update(id, updated));
    }

}