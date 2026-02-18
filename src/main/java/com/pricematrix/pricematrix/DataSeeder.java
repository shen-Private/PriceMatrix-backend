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

        Category c1 = new Category(); c1.setName("飲料");
        Category c2 = new Category(); c2.setName("零食");
        Category c3 = new Category(); c3.setName("日用品");
        categoryRepository.save(c1);
        categoryRepository.save(c2);
        categoryRepository.save(c3);

        Customer cu1 = new Customer(); cu1.setName("張三");
        Customer cu2 = new Customer(); cu2.setName("李四");
        Customer cu3 = new Customer(); cu3.setName("王五");
        customerRepository.save(cu1);
        customerRepository.save(cu2);
        customerRepository.save(cu3);

        Product p1 = new Product(); p1.setName("可樂"); p1.setCategory(c1);
        Product p2 = new Product(); p2.setName("綠茶"); p2.setCategory(c1);
        Product p3 = new Product(); p3.setName("洋芋片"); p3.setCategory(c2);
        Product p4 = new Product(); p4.setName("衛生紙"); p4.setCategory(c3);
        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        productRepository.save(p4);

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
