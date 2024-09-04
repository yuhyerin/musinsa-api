package com.musinsa.style.repository;

import com.musinsa.style.controller.dto.category.CategoryItemDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.musinsa.style.repository.entity.QBrand.brand;
import static com.musinsa.style.repository.entity.QCategory.category;
import static com.musinsa.style.repository.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CategoryItemDto> findMinPriceProductsForEachCategory() {

        return jpaQueryFactory
                .select(Projections.constructor(
                        CategoryItemDto.class,
                        category.name,
                        brand.name,
                        product.price)
                )
                .from(product)
                .join(product.brand, brand)
                .join(product.category, category)
                .where(product.price.eq(
                        JPAExpressions
                                .select(product.price.min())
                                .from(product)
                                .where(product.category.id.eq(category.id))
                ))
                .orderBy(category.id.asc(), product.id.asc())
                .fetch();

    }
}
