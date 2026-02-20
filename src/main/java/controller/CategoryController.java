package com.pricematrix.pricematrix.controller;

import com.pricematrix.pricematrix.entity.Category;
import com.pricematrix.pricematrix.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController          // 告訴 Spring：這是一個 API Controller
@RequestMapping("/api/categories")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:6006"})
public class CategoryController {

    private final CategoryService CategoryService;

    public CategoryController(CategoryService CategoryService) {
        this.CategoryService = CategoryService;
    }

    // GET /Categorys → 取得所有客戶
    @GetMapping
    public List<Category> getAllCategorys() {
        return CategoryService.getAllCategorys();
    }
}