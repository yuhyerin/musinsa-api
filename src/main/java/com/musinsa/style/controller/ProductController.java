package com.musinsa.style.controller;

import com.musinsa.style.controller.common.MusinsaResponse;
import com.musinsa.style.controller.dto.brand.BrandMinTotalPriceResponse;
import com.musinsa.style.controller.dto.category.CategoriesMinPriceResponse;
import com.musinsa.style.controller.dto.minmax.CategoryMinMaxPriceResponse;
import com.musinsa.style.enums.ResponseCode;
import com.musinsa.style.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "카테고리 별 최저가 상품 정보(브랜드, 상품 가격, 총액) 조회 API")
    @GetMapping("/categories/min-prices")
    public ResponseEntity<MusinsaResponse<CategoriesMinPriceResponse>> getCategoryMinPrices() {
        return ResponseEntity.ok(
                MusinsaResponse.<CategoriesMinPriceResponse>builder()
                        .data(productService.getCategoryMinPrices())
                        .result(ResponseCode.SUCCESS.result)
                        .message(ResponseCode.SUCCESS.message)
                        .build()
        );
    }

    @Operation(summary = "동일 브랜드의 모든 카테고리 상품을 구매 시 최저가 구매 가능한 상품 정보(브랜드, 카테고리, 상품 가격, 총액) 조회 API")
    @GetMapping("/brands/min-total-price")
    public ResponseEntity<MusinsaResponse<BrandMinTotalPriceResponse>> getMinPrice() {
        return ResponseEntity.ok(
                MusinsaResponse.<BrandMinTotalPriceResponse>builder()
                        .data(productService.getBrandMinTotalPrice())
                        .result(ResponseCode.SUCCESS.result)
                        .message(ResponseCode.SUCCESS.message)
                        .build()
        );
    }

    @Operation(summary = "카테고리 이름으로 최저가, 최고가 상품 정보(브랜드, 상품 가격) 조회 API")
    @GetMapping("/categories/{categoryName}/min-max-price")
    public ResponseEntity<MusinsaResponse<CategoryMinMaxPriceResponse>> getMinPriceByCategoryName(@PathVariable("categoryName") String categoryName) {
        return ResponseEntity.ok(
                MusinsaResponse.<CategoryMinMaxPriceResponse>builder()
                        .data(productService.getCategoryMinMaxPrice(categoryName))
                        .result(ResponseCode.SUCCESS.result)
                        .message(ResponseCode.SUCCESS.message)
                        .build()
        );
    }

}
