package com.musinsa.style.repository;

import com.musinsa.style.repository.entity.Brand;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.musinsa.style.repository.entity.QBrand.brand;
import static com.musinsa.style.repository.entity.QCategory.category;
import static com.musinsa.style.repository.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class BrandCustomRepositoryImpl implements BrandCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Brand findBrandWithMinTotalPrice() {
        return jpaQueryFactory
                .select(brand)
                .from(product)
                .join(brand).on(product.brand.id.eq(brand.id))
                .groupBy(product.brand.id)
                .having(product.countDistinct().eq(totalCategoryCount())) // 모든 카테고리에 상품이 있는 브랜드만 포함
                .orderBy(product.price.sum().asc())
                .fetchFirst();
    }

    private long totalCategoryCount() {
        return jpaQueryFactory.select(category.count()).from(category).fetchOne();
    }
}
