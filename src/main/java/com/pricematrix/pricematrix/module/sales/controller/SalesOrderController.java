package com.pricematrix.pricematrix.module.sales.controller;

import com.pricematrix.pricematrix.module.sales.entity.SalesOrder;
import com.pricematrix.pricematrix.module.sales.entity.SalesShipment;
import com.pricematrix.pricematrix.module.sales.service.SalesOrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService orderService;

    // POST /api/orders/from-quote/{quoteId} → 報價單轉訂單
    @PostMapping("/from-quote/{quoteId}")
    public ResponseEntity<SalesOrder> convertFromQuote(
            @PathVariable Long quoteId,
            HttpServletRequest httpRequest) {
        String username = (String) httpRequest.getAttribute("username");
        return ResponseEntity.ok(orderService.convertFromQuote(quoteId, username));
    }

    // GET /api/orders → 查詢全部訂單
    @GetMapping
    public ResponseEntity<List<SalesOrder>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // GET /api/orders/{id} → 查詢單一訂單
    @GetMapping("/{id}")
    public ResponseEntity<SalesOrder> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // POST /api/orders/{id}/shipments → 建立出貨單
    @PostMapping("/{id}/shipments")
    public ResponseEntity<SalesShipment> createShipment(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(orderService.createShipment(
                id,
                body.get("carrier"),
                body.get("trackingNumber"),
                body.get("note")
        ));
    }

    // PATCH /api/orders/shipments/{shipmentId}/confirm → 出貨確認
    @PatchMapping("/shipments/{shipmentId}/confirm")
    public ResponseEntity<SalesShipment> confirmShipment(@PathVariable Long shipmentId) {
        return ResponseEntity.ok(orderService.confirmShipment(shipmentId));
    }

    // GET /api/orders/{id}/shipments → 查詢出貨單列表
    @GetMapping("/{id}/shipments")
    public ResponseEntity<List<SalesShipment>> getShipments(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getShipmentsByOrder(id));
    }
}