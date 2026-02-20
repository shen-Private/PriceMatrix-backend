package com.pricematrix.pricematrix.controller;

import com.pricematrix.pricematrix.entity.Product;
import com.pricematrix.pricematrix.service.ProductService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController          // 告訴 Spring：這是一個 API Controller
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:6006"})
public class ProductController {

    private final ProductService ProductService;

    public ProductController(ProductService ProductService) {
        this.ProductService = ProductService;
    }

    // GET /Products → 取得所有客戶
    @GetMapping
    public List<Product> getAllProducts() {
        return ProductService.getAllProducts();
    }
}