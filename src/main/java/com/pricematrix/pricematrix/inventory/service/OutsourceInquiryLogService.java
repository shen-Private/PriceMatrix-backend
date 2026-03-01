package com.pricematrix.pricematrix.inventory.service;

import com.pricematrix.pricematrix.inventory.entity.OutsourceInquiryLog;
import com.pricematrix.pricematrix.inventory.entity.InventoryItem;
import com.pricematrix.pricematrix.inventory.repository.OutsourceInquiryLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutsourceInquiryLogService {

    private final OutsourceInquiryLogRepository logRepository;
    private final InventoryItemService itemService;

    public List<OutsourceInquiryLog> getLogsByItemId(Long itemId) {
        return logRepository.findByItem_IdOrderByConfirmedAtDesc(itemId);
    }

    public OutsourceInquiryLog addLog(Long itemId, OutsourceInquiryLog log) {
        InventoryItem item = itemService.getItemById(itemId);
        log.setItem(item);
        return logRepository.save(log);
    }
}