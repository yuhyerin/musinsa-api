package com.musinsa.style.repository;

import com.musinsa.style.controller.dto.category.CategoryItemDto;

import java.util.List;

public interface ProductCustomRepository {

    // 카테고리 별 최저가 조회
    List<CategoryItemDto> findMinPriceProductsForEachCategory();

}
