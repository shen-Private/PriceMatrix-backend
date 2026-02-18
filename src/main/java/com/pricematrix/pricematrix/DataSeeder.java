package com.pricematrix.pricematrix;

import com.pricematrix.pricematrix.entity.*;
import com.pricematrix.pricematrix.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    public DataSeeder(CustomerRepository customerRepository,
                      CategoryRepository categoryRepository,
                      ProductRepository productRepository,
                      DiscountRepository discountRepository) {
        this.customerRepository = customerRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }

    @Override
    public void run(String... args) {
        // 已有資料就不重複插入
        if (customerRepository.count() > 0) return;

        // 分類
        Category c1 = categoryRepository.save(new Category(null, "飲料"));
        Category c2 = categoryRepository.save(new Category(null, "零食"));
        Category c3 = categoryRepository.save(new Category(null, "日用品"));

        // 客戶
        Customer cu1 = customerRepository.save(new Customer(null, "張三"));
        Customer cu2 = customerRepository.save(new Customer(null, "李四"));
        Customer cu3 = customerRepository.save(new Customer(null, "王五"));

        // 商品
        Product p1 = productRepository.save(new Product(null, "可樂", c1));
        Product p2 = productRepository.save(new Product(null, "綠茶", c1));
        Product p3 = productRepository.save(new Product(null, "洋芋片", c2));
        Product p4 = productRepository.save(new Product(null, "衛生紙", c3));

        // 折扣
        discountRepository.save(new Discount(null, cu1, p1, new BigDecimal("0.85")));
        discountRepository.save(new Discount(null, cu1, p2, new BigDecimal("0.90")));
        discountRepository.save(new Discount(null, cu2, p1, new BigDecimal("0.80")));
        discountRepository.save(new Discount(null, cu2, p3, new BigDecimal("0.75")));
        discountRepository.save(new Discount(null, cu3, p4, new BigDecimal("0.95")));
    }
}
