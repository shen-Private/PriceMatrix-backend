package com.pricematrix.pricematrix.sales.controller;

import com.pricematrix.pricematrix.sales.entity.SalesProspect;
import com.pricematrix.pricematrix.sales.service.SalesProspectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prospects")
public class SalesProspectController {

    private final SalesProspectService service;

    public SalesProspectController(SalesProspectService service) {
        this.service = service;
    }

    @GetMapping
    public List<SalesProspect> getAll() {
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<SalesProspect> create(
            @RequestBody SalesProspect prospect,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        prospect.setCreatedBy(username);
        return ResponseEntity.ok(service.create(prospect));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesProspect> update(
            @PathVariable Long id,
            @RequestBody SalesProspect prospect,
            HttpServletRequest request) {
        return ResponseEntity.ok(service.update(id, prospect));
    }
}