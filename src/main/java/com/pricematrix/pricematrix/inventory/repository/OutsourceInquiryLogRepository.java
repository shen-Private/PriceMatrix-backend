package com.pricematrix.pricematrix.inventory.repository;

import com.pricematrix.pricematrix.inventory.entity.OutsourceInquiryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OutsourceInquiryLogRepository extends JpaRepository<OutsourceInquiryLog, Long> {
    List<OutsourceInquiryLog> findByItem_IdOrderByConfirmedAtDesc(Long itemId);
    Optional<OutsourceInquiryLog> findTopByItemIdOrderByConfirmedAtDesc(Long itemId);

}