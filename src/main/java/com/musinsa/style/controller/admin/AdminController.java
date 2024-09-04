package com.musinsa.style.controller.admin;

import com.musinsa.style.controller.common.MusinsaResponse;
import com.musinsa.style.controller.dto.brand.BrandCreateRequest;
import com.musinsa.style.controller.dto.brand.BrandDtoResponse;
import com.musinsa.style.controller.dto.product.ProductCreateRequest;
import com.musinsa.style.controller.dto.product.ProductDeleteRequest;
import com.musinsa.style.controller.dto.product.ProductDeleteResponse;
import com.musinsa.style.controller.dto.product.ProductDtoResponse;
import com.musinsa.style.enums.ResponseCode;
import com.musinsa.style.service.BrandService;
import com.musinsa.style.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BrandService brandService;
    private final ProductService productService;

    @Operation(summary = "브랜드 추가 API")
    @PostMapping("/brands")
    public ResponseEntity<MusinsaResponse<BrandDtoResponse>> createBrand(@RequestBody BrandCreateRequest request) {
        return ResponseEntity.ok(
                MusinsaResponse.<BrandDtoResponse>builder()
                        .data(brandService.createBrand(request.getBrandName()))
                        .result(ResponseCode.SUCCESS.result)
                        .message(ResponseCode.SUCCESS.message)
                        .build()
        );
    }

    @Operation(summary = "브랜드 업데이트 API")
    @PutMapping("/brands/{brandId}")
    public ResponseEntity<MusinsaResponse<BrandDtoResponse>> updateBrand(@PathVariable long brandId, @RequestBody BrandCreateRequest request) {
        return ResponseEntity.ok(
                MusinsaResponse.<BrandDtoResponse>builder()
                        .data(brandService.updateBrand(brandId, request.getBrandName()))
                        .result(ResponseCode.SUCCESS.result)
                        .message(ResponseCode.SUCCESS.message)
                        .build()
        );
    }

    @Operation(summary = "브랜드 삭제 API")
    @DeleteMapping("/brands/{brandId}")
    public ResponseEntity<MusinsaResponse<Long>> deleteBrand(@PathVariable long brandId) {
        brandService.deleteBrand(brandId);
        return ResponseEntity.ok(
                MusinsaResponse.<Long>builder()
                        .data(brandId)
                        .result(ResponseCode.SUCCESS.result)
                        .message(ResponseCode.SUCCESS.message)
                        .build()
        );
    }

    @Operation(summary = "상품 추가 API")
    @PostMapping("/products")
    public ResponseEntity<MusinsaResponse<ProductDtoResponse>> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        ProductDtoResponse product = productService.createProduct(request.getProductName(), request.getPrice(), request.getCategoryId(), request.getBrandId());
        return ResponseEntity.ok(
                MusinsaResponse.<ProductDtoResponse>builder()
                        .data(product)
                        .result(ResponseCode.SUCCESS.result)
                        .message(ResponseCode.SUCCESS.message)
                        .build()
        );
    }

    @Operation(summary = "상품 업데이트 API")
    @PutMapping("/products/{productId}")
    public ResponseEntity<MusinsaResponse<ProductDtoResponse>> updateProduct(@PathVariable long productId, @Valid @RequestBody ProductCreateRequest request) {
        ProductDtoResponse product = productService.updateProduct(productId, request.getProductName(), request.getPrice(), request.getCategoryId(), request.getBrandId());
        return ResponseEntity.ok(
                MusinsaResponse.<ProductDtoResponse>builder()
                        .data(product)
                        .result(ResponseCode.SUCCESS.result)
                        .message(ResponseCode.SUCCESS.message)
                        .build()
        );
    }

    @Operation(summary = "상품 삭제 API")
    @DeleteMapping("/products")
    public ResponseEntity<MusinsaResponse<ProductDeleteResponse>> deleteProducts(@Valid @RequestBody ProductDeleteRequest request) {
        return ResponseEntity.ok(
                MusinsaResponse.<ProductDeleteResponse>builder()
                        .data(productService.deleteProduct(request.getProductIds()))
                        .result(ResponseCode.SUCCESS.result)
                        .message(ResponseCode.SUCCESS.message)
                        .build()
        );
    }
}
