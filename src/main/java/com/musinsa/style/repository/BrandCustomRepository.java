package com.musinsa.style.repository;

import com.musinsa.style.repository.entity.Brand;

public interface BrandCustomRepository {
    // 모든 카테고리의 상품 구매 시 총합 최저가 브랜드 조회
    Brand findBrandWithMinTotalPrice();
}
