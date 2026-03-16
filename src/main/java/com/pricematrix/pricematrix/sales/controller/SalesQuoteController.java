package com.pricematrix.pricematrix.sales.controller;

import com.pricematrix.pricematrix.sales.dto.CreateQuoteRequest;
import com.pricematrix.pricematrix.sales.entity.SalesQuote;
import com.pricematrix.pricematrix.sales.service.SalesQuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pricematrix.pricematrix.sales.dto.UpdateQuoteStatusRequest;
import java.util.List;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class SalesQuoteController {

    private final SalesQuoteService quoteService;

    // 建立報價單
    @PostMapping
    public ResponseEntity<SalesQuote> createQuote(
            @RequestBody CreateQuoteRequest request,
            @CookieValue(name = "username", defaultValue = "unknown") String username) {
        return ResponseEntity.ok(quoteService.createQuote(request, username));
    }

    // 查詢全部報價單
    @GetMapping
    public ResponseEntity<List<SalesQuote>> getAllQuotes() {
        return ResponseEntity.ok(quoteService.getAllQuotes());
    }

    // 查詢單一報價單
    @GetMapping("/{id}")
    public ResponseEntity<SalesQuote> getQuoteById(@PathVariable Long id) {
        return ResponseEntity.ok(quoteService.getQuoteById(id));
    }

    // 更新狀態（DRAFT → SENT → CONVERTED / CANCELLED）
    @PatchMapping("/{id}/status")
    public ResponseEntity<SalesQuote> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateQuoteStatusRequest request) {
        return ResponseEntity.ok(quoteService.updateStatus(id, request.getStatus()));
    }

    // 建立修正版
    @PostMapping("/{id}/revise")
    public ResponseEntity<SalesQuote> createRevision(
            @PathVariable Long id,
            @CookieValue(name = "username", defaultValue = "unknown") String username) {
        return ResponseEntity.ok(quoteService.createRevision(id, username));
    }

    // 更新報價單內容（DRAFT 才能編輯）
    @PutMapping("/{id}")
    public ResponseEntity<SalesQuote> updateQuote(
            @PathVariable Long id,
            @RequestBody CreateQuoteRequest request) {
        return ResponseEntity.ok(quoteService.updateQuote(id, request));
    }
}