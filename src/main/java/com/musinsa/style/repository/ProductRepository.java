package com.musinsa.style.repository;

import com.musinsa.style.repository.entity.Brand;
import com.musinsa.style.repository.entity.Category;
import com.musinsa.style.repository.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findTop1ByCategoryOrderByPriceAsc(Category category); // 최저가 상품 조회
    Product findTop1ByCategoryOrderByPriceDesc(Category category); // 최고가 상품 조회

    List<Product> findAllByBrand(Brand brand);
    List<Product> findAllByBrandId(long brandId);

    @Modifying
    @Query("delete from Product p where p.id in :productIds")
    void deleteAllByIds(List<Long> productIds);
}
