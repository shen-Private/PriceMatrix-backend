package com.pricematrix.pricematrix.service;

import com.pricematrix.pricematrix.entity.Discount;
import com.pricematrix.pricematrix.entity.Product;
import com.pricematrix.pricematrix.repository.DiscountRepository;
import org.springframework.stereotype.Service;
import com.pricematrix.pricematrix.repository.ProductRepository;
import java.util.List;

@Service  // 告訴 Spring：這是一個 Service 元件
public class DiscountService {

    // 注入 Repository（Spring 自動幫你 new）
    private final DiscountRepository DiscountRepository;
    private final ProductRepository productRepository;

    public DiscountService(DiscountRepository DiscountRepository, ProductRepository productRepository) {
        this.DiscountRepository = DiscountRepository;
        this.productRepository = productRepository;
    }

    // 取得所有客戶
    public List<Discount> getAllDiscounts() {
        return DiscountRepository.findAll();
    }
    // 按客戶ID取得折扣清單

    // 按客戶ID取得折扣清單（categoryId 可選，null = 全部）
    public List<Discount> getDiscountsByCustomerId(Long customerId, Long categoryId) {
        if (categoryId != null) {
            // 有選分類：同時過濾客戶 + 分類
            return DiscountRepository.findByCustomerIdAndProductCategoryId(customerId, categoryId);
        } else {
            // 沒選分類：只過濾客戶
            return DiscountRepository.findByCustomerId(customerId   );
        }
    }
    // 修改折扣率
    public Discount updateDiscount(Long id, Discount updatedDiscount) {
        Discount existing = DiscountRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("找不到折扣記錄：" + id));

        existing.setDiscountRatio(updatedDiscount.getDiscountRatio()); // 只更新折扣率
        return DiscountRepository.save(existing); // 存回資料庫
    }
    // 刪除折扣記錄
    public void deleteDiscount(Long id) {
        DiscountRepository.deleteById(id);
    }
    // 新增折扣記錄
    public Discount createDiscount(Discount discount) {
        // 先用商品名稱查出真正的 Product 物件
        String productName = discount.getProduct().getName();
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new RuntimeException("找不到商品：" + productName));

        // 把真正的 Product 設回去
        discount.setProduct(product);

        return DiscountRepository.save(discount);
    }
}
