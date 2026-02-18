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
        if (customerRepository.count() > 0) return;

        Category c1 = categoryRepository.save(new Category("飲料"));
        Category c2 = categoryRepository.save(new Category("零食"));
        Category c3 = categoryRepository.save(new Category("日用品"));

        Customer cu1 = customerRepository.save(new Customer("張三", null));
        Customer cu2 = customerRepository.save(new Customer("李四", null));
        Customer cu3 = customerRepository.save(new Customer("王五", null));

        Product p1 = productRepository.save(new Product("可樂", new BigDecimal("100"), c1));
        Product p2 = productRepository.save(new Product("綠茶", new BigDecimal("80"), c1));
        Product p3 = productRepository.save(new Product("洋芋片", new BigDecimal("50"), c2));
        Product p4 = productRepository.save(new Product("衛生紙", new BigDecimal("120"), c3));

        Discount d1 = new Discount(); d1.setCustomer(cu1); d1.setProduct(p1); d1.setDiscountRatio(new BigDecimal("0.85"));
        Discount d2 = new Discount(); d2.setCustomer(cu1); d2.setProduct(p2); d2.setDiscountRatio(new BigDecimal("0.90"));
        Discount d3 = new Discount(); d3.setCustomer(cu2); d3.setProduct(p1); d3.setDiscountRatio(new BigDecimal("0.80"));
        Discount d4 = new Discount(); d4.setCustomer(cu2); d4.setProduct(p3); d4.setDiscountRatio(new BigDecimal("0.75"));
        Discount d5 = new Discount(); d5.setCustomer(cu3); d5.setProduct(p4); d5.setDiscountRatio(new BigDecimal("0.95"));
        discountRepository.save(d1);
        discountRepository.save(d2);
        discountRepository.save(d3);
        discountRepository.save(d4);
        discountRepository.save(d5);
    }
}
