package com.musinsa.style.service;

import com.musinsa.style.controller.dto.brand.BrandDtoResponse;
import com.musinsa.style.exception.BrandDeleteConflictException;
import com.musinsa.style.exception.BrandNotFoundException;
import com.musinsa.style.repository.BrandRepository;
import com.musinsa.style.repository.ProductRepository;
import com.musinsa.style.repository.entity.Brand;
import com.musinsa.style.repository.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    public BrandDtoResponse createBrand(String brandName) {
        Brand brand = brandRepository.save(Brand.builder().name(brandName).build());
        return new BrandDtoResponse(brand);
    }

    public BrandDtoResponse updateBrand(long brandId, String brandName) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));

        brand.setName(brandName);
        brandRepository.save(brand);

        return new BrandDtoResponse(brand);
    }

    public void deleteBrand(long brandId) {
        List<Product> products = productRepository.findAllByBrandId(brandId);
        if (!products.isEmpty()) {
            throw new BrandDeleteConflictException(); // 브랜드 등록된 상품이 있으면 브랜드 삭제 불가
        }

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));
        brandRepository.delete(brand);
    }
}
