package com.pricematrix.pricematrix.inventory.controller;

import com.pricematrix.pricematrix.inventory.entity.OutsourceInquiryLog;
import com.pricematrix.pricematrix.inventory.service.OutsourceInquiryLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventory/inquiries")
@RequiredArgsConstructor
public class OutsourceInquiryLogController {

    private final OutsourceInquiryLogService logService;

    @GetMapping("/item/{itemId}")
    public List<OutsourceInquiryLog> getLogs(@PathVariable Long itemId) {
        return logService.getLogsByItemId(itemId);
    }

    @PostMapping
    public OutsourceInquiryLog addLog(@RequestBody OutsourceInquiryLogRequest request) {
        OutsourceInquiryLog log = new OutsourceInquiryLog();
        log.setConfirmedBy(request.confirmedBy());
        log.setQuantity(request.quantity());
        log.setNote(request.note());
        log.setConfirmedAt(java.time.LocalDateTime.now());
        return logService.addLog(request.itemId(), log);
    }

    // Controller 內部加這個 record（接收前端傳來的 JSON）
    record OutsourceInquiryLogRequest(
            Long itemId,
            String confirmedBy,
            Integer quantity,
            String note
    ) {}
}