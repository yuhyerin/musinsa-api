package com.musinsa.style.service;

import com.musinsa.style.controller.dto.brand.BrandMinTotalPriceResponse;
import com.musinsa.style.controller.dto.category.CategoriesMinPriceResponse;
import com.musinsa.style.controller.dto.category.CategoryItemDto;
import com.musinsa.style.controller.dto.minmax.CategoryMinMaxPriceResponse;
import com.musinsa.style.controller.dto.product.ProductDeleteResponse;
import com.musinsa.style.exception.CategoryNotFoundException;
import com.musinsa.style.exception.ProductNotFoundException;
import com.musinsa.style.repository.BrandCustomRepository;
import com.musinsa.style.repository.CategoryRepository;
import com.musinsa.style.repository.ProductCustomRepository;
import com.musinsa.style.repository.ProductRepository;
import com.musinsa.style.repository.entity.Brand;
import com.musinsa.style.repository.entity.Category;
import com.musinsa.style.repository.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductCustomRepository productCustomRepository;
    @Mock
    private BrandCustomRepository brandCustomRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("카테고리 별 최저가 조회 시 중복된 최저가 상품이 있는 경우 카테고리,상품 id 오름차 순 정렬 마지막 상품 정보 조회")
    void testGetCategoryMinPrices_WithDuplicates() {
        // Given
        List<CategoryItemDto> mockMinPriceProductDtoList = List.of(
                new CategoryItemDto("상의", "C", 10000),
                new CategoryItemDto("아우터", "E", 5000),
                new CategoryItemDto("바지", "D", 3000),
                new CategoryItemDto("스니커즈", "A", 9000),
                new CategoryItemDto("스니커즈", "G", 9000), // 최저가 중복 상품 존재
                new CategoryItemDto("가방", "A", 2000),
                new CategoryItemDto("모자", "D", 1500),
                new CategoryItemDto("양말", "I", 1700),
                new CategoryItemDto("액세서리", "F", 1900)
        );

        // when
        when(productCustomRepository.findMinPriceProductsForEachCategory()).thenReturn(mockMinPriceProductDtoList);
        CategoriesMinPriceResponse response = productService.getCategoryMinPrices();

        // then
        assertThat(response.getMinPrice().getCategories()).hasSize(mockMinPriceProductDtoList.size() - 1);
        assertThat(response.getMinPrice().getTotalPrice()).isEqualTo(mockMinPriceProductDtoList.stream().mapToLong(CategoryItemDto::getPrice).sum() - 9000L);
        assertThat(response.getMinPrice().getCategories().get(3).getBrandName()).isEqualTo("G"); // 중복 상품 존재하는 경우 카테고리 id, 상품 id 오름차 순 정렬 마지막 상품 정보 조회
        log.info("카테고리 별 최저가 조회 시 총합 totalPrice = {}", response.getMinPrice().getTotalPrice());
    }

    @Test
    @DisplayName("모든 카테고리 상품 구매시 가격 총합 최저가 브랜드 정상 조회")
    void testGetBrandMinTotalPrice_ok() {
        // Set up a brand
        Brand minTotalPriceBrand = new Brand(1L, "D");

        // Set up categories
        Category[] categories =  {
                new Category(1L, "상의")
                , new Category(2L, "아우터")
                , new Category(3L, "바지")
                , new Category(4L, "스니커즈")
                , new Category(5L, "가방")
                , new Category(6L, "모자")
                , new Category(7L, "양말")
                , new Category(8L, "액세서리")
        };

        // Set up products
        List<Product> minPriceProducts = Arrays.asList(
                new Product(1L, "상의D", 10100L, categories[0], minTotalPriceBrand)
                , new Product(2L, "아우터D", 5100L, categories[1], minTotalPriceBrand)
                , new Product(3L, "바지D", 3000L, categories[2], minTotalPriceBrand)
                , new Product(4L, "스니커즈D", 9500L, categories[3], minTotalPriceBrand)
                , new Product(5L, "가방D", 2500L, categories[4], minTotalPriceBrand)
                , new Product(6L, "모자D", 1500L, categories[5], minTotalPriceBrand)
                , new Product(7L, "양말D", 2400L, categories[6], minTotalPriceBrand)
                , new Product(8L, "액세서리D", 2000L, categories[7], minTotalPriceBrand)
        );

        // Mocking brandCustomRepository to return the brand with the lowest total price
        when(brandCustomRepository.findBrandWithMinTotalPrice()).thenReturn(minTotalPriceBrand);

        // Mocking productRepository to return products belonging to the cheapest brand
        when(productRepository.findAllByBrand(minTotalPriceBrand)).thenReturn(minPriceProducts);

        // Execute the method to be tested
        BrandMinTotalPriceResponse response = productService.getBrandMinTotalPrice();

        // Validate the response
        assertEquals(minTotalPriceBrand.getName(), response.getMinPrice().getBrandName());
        assertEquals(categories.length, response.getMinPrice().getCategories().size());
        assertEquals(minPriceProducts.stream().mapToLong(Product::getPrice).sum(), response.getMinPrice().getTotalPrice());
        log.info("모든 카테고리 상품 구매 시 최저가 브랜드 brandName = {}, 총합 totalPrice = {}", response.getMinPrice().getBrandName(), response.getMinPrice().getTotalPrice());
    }

    @Test
    @DisplayName("카테고리 이름으로 최저가, 최고가 상품 정상 조회")
    void testGetCategoryMinMaxPrice_ok() {
        // Mocking categoryRepository to return the category
        Category category = new Category(1L, "상의");
        when(categoryRepository.findFirstByName("상의")).thenReturn(Optional.of(category));

        // Mocking productRepository to return the min and max price products
        Brand minPriceBrand = new Brand(1L, "C");
        Product minPriceProduct = new Product(1L, "상의C", 10000L, category, minPriceBrand);

        Brand maxPriceBrand = new Brand(2L, "I");
        Product maxPriceProduct = new Product(2L, "상의I", 11400L, category, maxPriceBrand);

        when(productRepository.findTop1ByCategoryOrderByPriceAsc(category)).thenReturn(minPriceProduct);
        when(productRepository.findTop1ByCategoryOrderByPriceDesc(category)).thenReturn(maxPriceProduct);

        // Execute the method to be tested
        CategoryMinMaxPriceResponse response = productService.getCategoryMinMaxPrice("상의");

        // Validate the response
        assertEquals("상의", response.getCategoryName());
        assertEquals("C", response.getMinPrice().getBrandName());
        assertEquals(10000L, response.getMinPrice().getPrice());
        assertEquals("I", response.getMaxPrice().getBrandName());
        assertEquals(11400L, response.getMaxPrice().getPrice());
    }

    @Test
    @DisplayName("카테고리 이름으로 최저가, 최고가 조회 시 카테고리 없는 경우 예외 발생")
    void testGetCategoryMinMaxPrice_CategoryNotFound() {
        // Mocking categoryRepository to return empty optional
        when(categoryRepository.findFirstByName("없는카테고리")).thenReturn(Optional.empty());

        // Validate the exception
        assertThrows(CategoryNotFoundException.class, () -> productService.getCategoryMinMaxPrice("없는카테고리"));
    }

    @Test
    @DisplayName("상품 생성 시 카테고리 존재하지 않는 경우 예외 발생")
    void testCreateProduct_CategoryNotFound() {
        // Given
        long nonExistentCategoryId = 999L;
        String productName = "New Product";
        long price = 1000L;
        long brandId = 1L;

        // Mocking categoryRepository to return empty optional for non-existent category
        when(categoryRepository.findById(nonExistentCategoryId)).thenReturn(Optional.empty());

        // Validate the exception
        assertThrows(CategoryNotFoundException.class, () ->
                productService.createProduct(productName, price, nonExistentCategoryId, brandId)
        );
    }

    @Test
    @DisplayName("상품 업데이트 시 해당 상품이 존재하지 않는 경우 예외 발생")
    void testUpdateProduct_ProductNotFound() {
        // Given
        long nonExistentProductId = 999L;
        String productName = "Updated Product";
        long price = 1500L;
        long categoryId = 1L;
        long brandId = 1L;

        // Mocking productRepository to return empty optional for non-existent product
        when(productRepository.findById(nonExistentProductId)).thenReturn(Optional.empty());

        // Validate the exception
        assertThrows(ProductNotFoundException.class, () ->
                productService.updateProduct(nonExistentProductId, productName, price, categoryId, brandId)
        );
    }

    @Test
    @DisplayName("상품 삭제 시 일부 상품 아이디가 존재하지 않는 경우 failProductIds 값에 응답을 준다")
    void testDeleteProduct_WithMissingIds() {
        // Given
        List<Long> productIds = List.of(1L, 2L, 999L); // 999L은 존재하지 않는 ID로 가정
        Product product1 = new Product(1L, "Product 1", 1000L, new Category(), new Brand());
        Product product2 = new Product(2L, "Product 2", 1500L, new Category(), new Brand());

        // Mocking productRepository to return only existing products
        when(productRepository.findAllById(productIds)).thenReturn(List.of(product1, product2));

        // Execute the method to be tested
        ProductDeleteResponse response = productService.deleteProduct(productIds);

        // Validate the response
        assertEquals(2, response.getSuccessProductIds().size());
        assertTrue(response.getSuccessProductIds().containsAll(List.of(1L, 2L)));
        assertEquals(1, response.getFailProductIds().size());
        assertTrue(response.getFailProductIds().contains(999L));
    }
}