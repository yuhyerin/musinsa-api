package com.musinsa.style.service;

import com.musinsa.style.repository.CategoryRepository;
import com.musinsa.style.repository.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Cacheable(value = "allCategories")
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }
}
