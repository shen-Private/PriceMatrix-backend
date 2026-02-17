package com.pricematrix.pricematrix.service;

import com.pricematrix.pricematrix.entity.Category;
import com.pricematrix.pricematrix.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service  // 告訴 Spring：這是一個 Service 元件
public class CategoryService {

    // 注入 Repository（Spring 自動幫你 new）
    private final CategoryRepository CategoryRepository;

    public CategoryService(CategoryRepository CategoryRepository) {
        this.CategoryRepository = CategoryRepository;
    }

    // 取得所有客戶
    public List<Category> getAllCategorys() {
        return CategoryRepository.findAll();
    }
}