package com.musinsa.style.controller.dto.product;

import com.musinsa.style.repository.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductDtoResponse {
    private long productId;
    private String productName;
    private long price;
    private long categoryId;
    private String categoryName;
    private long brandId;
    private String brandName;

    public ProductDtoResponse(Product product) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.price = product.getPrice();
        this.categoryId = product.getCategory().getId();
        this.categoryName = product.getCategory().getName();
        this.brandId = product.getBrand().getId();
        this.brandName = product.getBrand().getName();
    }
}
