package com.pricematrix.pricematrix.service;

import com.pricematrix.pricematrix.entity.Product;
import com.pricematrix.pricematrix.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service  // 告訴 Spring：這是一個 Service 元件
public class ProductService {

    // 注入 Repository（Spring 自動幫你 new）
    private final ProductRepository ProductRepository;

    public ProductService(ProductRepository ProductRepository) {
        this.ProductRepository = ProductRepository;
    }

    // 取得所有客戶
    public List<Product> getAllProducts() {
        return ProductRepository.findAll();
    }
}