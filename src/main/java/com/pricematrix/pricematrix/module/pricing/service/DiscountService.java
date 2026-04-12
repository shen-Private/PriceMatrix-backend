package com.pricematrix.pricematrix.module.pricing.service;

import com.pricematrix.pricematrix.common.audit.Entity.AuditLog;
import com.pricematrix.pricematrix.common.audit.Service.AuditLogService;
import com.pricematrix.pricematrix.module.pricing.entity.Discount;
import com.pricematrix.pricematrix.module.pricing.entity.Product;
import com.pricematrix.pricematrix.module.pricing.repository.DiscountRepository;
import com.pricematrix.pricematrix.module.pricing.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository DiscountRepository;
    private final ProductRepository productRepository;
    private final AuditLogService auditLogService;  // ← 換掉舊的

    public List<Discount> getAllDiscounts() {
        return DiscountRepository.findAll();
    }

    public List<Discount> getDiscountsByCustomerId(Long customerId, Long categoryId) {
        if (categoryId != null) {
            return DiscountRepository.findByCustomerIdAndProductCategoryId(customerId, categoryId);
        } else {
            return DiscountRepository.findByCustomerId(customerId);
        }
    }

    public Discount updateDiscount(Long id, Discount updatedDiscount, HttpServletRequest request) {
        Discount existing = DiscountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到折扣記錄：" + id));

        String operatedBy = (String) request.getAttribute("username");
        BigDecimal oldRatio = existing.getDiscountRatio();
        existing.setDiscountRatio(updatedDiscount.getDiscountRatio());
        Discount saved = DiscountRepository.save(existing);

        auditLogService.log("DISCOUNT", id, "discountRatio",
                oldRatio != null ? oldRatio.toString() : null,
                saved.getDiscountRatio().toString(),
                operatedBy);
        return saved;
    }

    public void batchUpdateDiscounts(Map<Long, BigDecimal> updates, HttpServletRequest request) {
        String operatedBy = (String) request.getAttribute("username");
        for (Map.Entry<Long, BigDecimal> entry : updates.entrySet()) {
            Long discountId = entry.getKey();
            BigDecimal newRatio = entry.getValue();

            Discount existing = DiscountRepository.findById(discountId)
                    .orElseThrow(() -> new RuntimeException("找不到折扣記錄：" + discountId));

            BigDecimal oldRatio = existing.getDiscountRatio();
            existing.setDiscountRatio(newRatio);
            DiscountRepository.save(existing);

            auditLogService.log("DISCOUNT", discountId, "discountRatio",
                    oldRatio != null ? oldRatio.toString() : null,
                    newRatio.toString(),
                    operatedBy);
        }
    }

    public void deleteDiscount(Long id) {
        DiscountRepository.deleteById(id);
    }

    public Discount createDiscount(Discount discount, HttpServletRequest request) {
        Long productId = discount.getProduct().getId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("找不到商品：" + productId));
        discount.setProduct(product);
        Discount saved = DiscountRepository.save(discount);

        String operatedBy = (String) request.getAttribute("username");
        auditLogService.log("DISCOUNT", saved.getId(), "discountRatio",
                null,
                saved.getDiscountRatio().toString(),
                operatedBy);
        return saved;
    }

    // ← 回傳型別改成 AuditLog
    public List<AuditLog> getAuditLogs(Long discountId) {
        return auditLogService.getLogs("DISCOUNT", discountId);
    }

    public Optional<Discount> findByCustomerAndProduct(Long customerId, Long productId) {
        return DiscountRepository.findByCustomerIdAndProductId(customerId, productId);
    }
}