package com.pricematrix.pricematrix.sales.service;

import com.pricematrix.pricematrix.sales.entity.SalesOrder;
import com.pricematrix.pricematrix.sales.entity.SalesQuote;
import com.pricematrix.pricematrix.sales.entity.SalesShipment;
import com.pricematrix.pricematrix.sales.repository.SalesOrderRepository;
import com.pricematrix.pricematrix.sales.repository.SalesShipmentRepository;
import com.pricematrix.pricematrix.sales.repository.SalesQuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOrderService {

    private final SalesOrderRepository orderRepository;
    private final SalesShipmentRepository shipmentRepository;
    private final SalesQuoteRepository quoteRepository;

    // 報價單轉訂單（SENT → CONVERTED，同時建立 SalesOrder）
    public SalesOrder convertFromQuote(Long quoteId, String createdBy) {
        SalesQuote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        if (!quote.getStatus().equals("SENT")) {
            throw new RuntimeException("只有 SENT 狀態的報價單才能轉為訂單");
        }

        // 檢查是否已經轉過
        orderRepository.findByQuoteId(quoteId).ifPresent(o -> {
            throw new RuntimeException("此報價單已建立訂單");
        });

        // 報價單狀態改為 CONVERTED
        quote.setStatus("CONVERTED");
        quoteRepository.save(quote);

        // 建立訂單
        SalesOrder order = new SalesOrder();
        order.setQuote(quote);
        order.setCustomer(quote.getCustomer());
        order.setCreatedBy(createdBy);
        return orderRepository.save(order);
    }

    // 查詢全部訂單
    public List<SalesOrder> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    // 查詢單一訂單
    public SalesOrder getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // 建立出貨單
    public SalesShipment createShipment(Long orderId, String carrier,
                                        String trackingNumber, String note) {
        SalesOrder order = getOrderById(orderId);

        SalesShipment shipment = new SalesShipment();
        shipment.setOrder(order);
        shipment.setCarrier(carrier);
        shipment.setTrackingNumber(trackingNumber);
        shipment.setNote(note);
        return shipmentRepository.save(shipment);
    }

    // 出貨確認（PREPARING → SHIPPED）
    public SalesShipment confirmShipment(Long shipmentId) {
        SalesShipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
        shipment.setStatus("SHIPPED");
        shipment.setShippedAt(LocalDateTime.now());

        // 所有出貨單都 SHIPPED → 訂單改為 COMPLETED
        SalesOrder order = shipment.getOrder();
        boolean allShipped = shipmentRepository.findByOrderId(order.getId())
                .stream().allMatch(s -> s.getStatus().equals("SHIPPED"));
        if (allShipped) {
            order.setStatus("COMPLETED");
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);
        }

        return shipmentRepository.save(shipment);
    }

    // 查詢訂單的出貨單列表
    public List<SalesShipment> getShipmentsByOrder(Long orderId) {
        return shipmentRepository.findByOrderId(orderId);
    }
}