package com.pricematrix.pricematrix.pricing.service;

import com.pricematrix.pricematrix.pricing.entity.Discount;
import com.pricematrix.pricematrix.pricing.entity.DiscountAuditLog;
import com.pricematrix.pricematrix.pricing.entity.Product;
import com.pricematrix.pricematrix.pricing.repository.DiscountAuditLogRepository;
import com.pricematrix.pricematrix.pricing.repository.DiscountRepository;
import com.pricematrix.pricematrix.pricing.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DiscountService {

    private final DiscountRepository DiscountRepository;
    private final ProductRepository productRepository;
    private final DiscountAuditLogRepository auditLogRepository;

    public DiscountService(DiscountRepository DiscountRepository,
                           ProductRepository productRepository,
                           DiscountAuditLogRepository auditLogRepository) {
        this.DiscountRepository = DiscountRepository;
        this.productRepository = productRepository;
        this.auditLogRepository = auditLogRepository;
    }

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

    // 單筆修改（原本的邏輯 + 寫 audit log）
    public Discount updateDiscount(Long id, Discount updatedDiscount) {
        Discount existing = DiscountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到折扣記錄：" + id));

        BigDecimal oldRatio = existing.getDiscountRatio();
        existing.setDiscountRatio(updatedDiscount.getDiscountRatio());
        Discount saved = DiscountRepository.save(existing);

        // 寫 audit log
        writeAuditLog(id, oldRatio, saved.getDiscountRatio(), "UPDATE");

        return saved;
    }

    // 批次修改：傳入 Map<discountId, newRatio>
    public void batchUpdateDiscounts(Map<Long, BigDecimal> updates) {
        for (Map.Entry<Long, BigDecimal> entry : updates.entrySet()) {
            Long discountId = entry.getKey();
            BigDecimal newRatio = entry.getValue();

            Discount existing = DiscountRepository.findById(discountId)
                    .orElseThrow(() -> new RuntimeException("找不到折扣記錄：" + discountId));

            BigDecimal oldRatio = existing.getDiscountRatio();
            existing.setDiscountRatio(newRatio);
            DiscountRepository.save(existing);

            writeAuditLog(discountId, oldRatio, newRatio, "BATCH_UPDATE");
        }
    }

    public void deleteDiscount(Long id) {
        DiscountRepository.deleteById(id);
    }

    public Discount createDiscount(Discount discount) {
        Long productId = discount.getProduct().getId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("找不到商品：" + productId));
        discount.setProduct(product);
        Discount saved = DiscountRepository.save(discount);

        // 新增也寫 log
        writeAuditLog(saved.getId(), null, saved.getDiscountRatio(), "CREATE");

        return saved;
    }

    // 查某筆折扣的變更歷史
    public List<DiscountAuditLog> getAuditLogs(Long discountId) {
        return auditLogRepository.findByDiscountIdOrderByOperatedAtDesc(discountId);
    }

    // 內部工具方法：寫 audit log
    private void writeAuditLog(Long discountId, BigDecimal oldRatio, BigDecimal newRatio, String action) {
        DiscountAuditLog log = new DiscountAuditLog();
        log.setDiscountId(discountId);
        log.setOldRatio(oldRatio);
        log.setNewRatio(newRatio);
        log.setAction(action);
        log.setOperatedBy("系統使用者"); // 之後接登入系統再換
        auditLogRepository.save(log);
    }
    public Optional<Discount> findByCustomerAndProduct(Long customerId, Long productId) {
        return DiscountRepository.findByCustomerIdAndProductId(customerId, productId);
    }
}