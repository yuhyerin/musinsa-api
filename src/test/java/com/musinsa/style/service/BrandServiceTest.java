package com.musinsa.style.service;

import com.musinsa.style.controller.dto.brand.BrandDtoResponse;
import com.musinsa.style.exception.BrandDeleteConflictException;
import com.musinsa.style.exception.BrandNotFoundException;
import com.musinsa.style.repository.BrandRepository;
import com.musinsa.style.repository.ProductRepository;
import com.musinsa.style.repository.entity.Brand;
import com.musinsa.style.repository.entity.Category;
import com.musinsa.style.repository.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private BrandService brandService;

    @Test
    @DisplayName("브랜드 생성 시 정상적으로 BrandDtoResponse 반환")
    void testCreateBrand() {
        // Given
        String brandName = "newBrand";
        Brand brand = new Brand(1L, brandName);
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        // When
        BrandDtoResponse response = brandService.createBrand(brandName);

        // Then
        assertNotNull(response);
        assertEquals(brandName, response.getBrandName());
        verify(brandRepository, times(1)).save(any(Brand.class));
    }

    @Test
    @DisplayName("브랜드 업데이트 시 정상적으로 BrandDtoResponse 반환")
    void testUpdateBrand() {
        // Given
        long brandId = 1L;
        Brand brand = new Brand(brandId, "old Brand Name");

        String updatedBrandName = "Updated Brand";
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        // When
        BrandDtoResponse response = brandService.updateBrand(brandId, updatedBrandName);

        // Then
        assertNotNull(response);
        assertEquals(updatedBrandName, response.getBrandName());
        verify(brandRepository, times(1)).findById(brandId);
        verify(brandRepository, times(1)).save(brand);
    }

    @Test
    @DisplayName("브랜드 업데이트 시 해당 브랜드가 존재하지 않을 경우 예외 발생")
    void testUpdateBrand_BrandNotFound() {
        // Given
        long nonExistentBrandId = 999L;
        String brandName = "Non-existent Brand";
        when(brandRepository.findById(nonExistentBrandId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BrandNotFoundException.class, () -> brandService.updateBrand(nonExistentBrandId, brandName));
        verify(brandRepository, times(1)).findById(nonExistentBrandId);
        verify(brandRepository, never()).save(any(Brand.class));
    }

    @Test
    @DisplayName("브랜드 삭제 시 해당 브랜드가 존재하지 않을 경우 예외 발생")
    void testDeleteBrand_BrandNotFound() {
        // Given
        long nonExistentBrandId = 999L;
        when(brandRepository.findById(nonExistentBrandId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BrandNotFoundException.class, () -> brandService.deleteBrand(nonExistentBrandId));
        verify(brandRepository, times(1)).findById(nonExistentBrandId);
        verify(brandRepository, never()).delete(any(Brand.class));
    }

    @Test
    @DisplayName("브랜드 삭제 시 등록된 상품이 있으면 예외 발생")
    void testDeleteBrand_WithExistingProducts() {
        // Given
        long brandId = 1L;
        Brand brand = new Brand(brandId, "old Brand Name");
        Category category = new Category(1L, "상의");
        Product product = new Product(1L, "상의D", 10100L, category, brand);
        when(productRepository.findAllByBrandId(brandId)).thenReturn(List.of(product));

        // When & Then
        assertThrows(BrandDeleteConflictException.class, () -> brandService.deleteBrand(brandId));
        verify(productRepository, times(1)).findAllByBrandId(brandId);
        verify(brandRepository, never()).delete(any(Brand.class));
    }

    @Test
    @DisplayName("브랜드 삭제 시 정상적으로 브랜드 삭제")
    void testDeleteBrand_NoProducts() {
        // Given
        long brandId = 1L;
        Brand brand = new Brand(brandId, "old Brand Name");
        when(productRepository.findAllByBrandId(brandId)).thenReturn(Collections.emptyList());
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));

        // When
        brandService.deleteBrand(brandId);

        // Then
        verify(productRepository, times(1)).findAllByBrandId(brandId);
        verify(brandRepository, times(1)).findById(brandId);
        verify(brandRepository, times(1)).delete(brand);
    }
}