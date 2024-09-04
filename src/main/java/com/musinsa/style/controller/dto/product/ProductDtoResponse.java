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
    private String categoryName;
    private String brandName;

    public ProductDtoResponse(Product product) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.price = product.getPrice();
        this.categoryName = product.getCategory().getName();
        this.brandName = product.getBrand().getName();
    }
}
