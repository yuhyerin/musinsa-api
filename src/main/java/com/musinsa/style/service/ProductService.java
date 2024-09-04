package com.musinsa.style.service;

import com.musinsa.style.controller.dto.brand.BrandMinTotalPriceResponse;
import com.musinsa.style.controller.dto.category.CategoriesMinPriceResponse;
import com.musinsa.style.controller.dto.category.CategoryItemDto;
import com.musinsa.style.controller.dto.minmax.CategoryMinMaxPrice;
import com.musinsa.style.controller.dto.minmax.CategoryMinMaxPriceResponse;
import com.musinsa.style.controller.dto.product.ProductDeleteResponse;
import com.musinsa.style.controller.dto.product.ProductDtoResponse;
import com.musinsa.style.enums.ResponseCode;
import com.musinsa.style.exception.BrandNotFoundException;
import com.musinsa.style.exception.CategoryNotFoundException;
import com.musinsa.style.exception.ProductNotFoundException;
import com.musinsa.style.repository.BrandCustomRepository;
import com.musinsa.style.repository.BrandRepository;
import com.musinsa.style.repository.CategoryRepository;
import com.musinsa.style.repository.ProductCustomRepository;
import com.musinsa.style.repository.ProductRepository;
import com.musinsa.style.repository.entity.Brand;
import com.musinsa.style.repository.entity.Category;
import com.musinsa.style.repository.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductCustomRepository productCustomRepository;
    private final BrandCustomRepository brandCustomRepository;

    @Cacheable(value = "categoryMinPrices")
    public CategoriesMinPriceResponse getCategoryMinPrices() {
        List<CategoryItemDto> minPriceProducts = productCustomRepository.findMinPriceProductsForEachCategory();
        List<CategoryItemDto> distinctMinPriceProducts = removeDuplicateMinPriceProducts(minPriceProducts); // 카테고리 내 최저가가 중복인 경우 처리
        return new CategoriesMinPriceResponse(distinctMinPriceProducts);
    }

    private List<CategoryItemDto> removeDuplicateMinPriceProducts(List<CategoryItemDto> minPriceProductDtos) {
        return minPriceProductDtos.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                item -> Objects.hash(item.getCategoryName(), item.getPrice()), // key
                                item -> item,
                                (existing, replacement) -> replacement,  // 중복 키가 있을 때 새로운 값으로 대체
                                LinkedHashMap::new // 기존 정렬 순서 유지
                        ),
                        map -> map.values().stream().collect(Collectors.toList())
                ));
    }

    @Cacheable(value = "brandMinTotalPrice")
    public BrandMinTotalPriceResponse getBrandMinTotalPrice() {

        Brand minTotalPriceBrand = brandCustomRepository.findBrandWithMinTotalPrice();
        List<Product> productList = productRepository.findAllByBrand(minTotalPriceBrand);

        BrandMinTotalPriceResponse response = new BrandMinTotalPriceResponse(minTotalPriceBrand.getName());
        productList.forEach(product -> {
            response.addCategory(product.getCategory().getName(), product.getPrice());
        });

        return response;
    }

    @Cacheable(value = "categoryMinMaxPrice")
    public CategoryMinMaxPriceResponse getCategoryMinMaxPrice(String categoryName) {

        Category category = categoryRepository.findFirstByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName));

        Product minPriceProduct = productRepository.findTop1ByCategoryOrderByPriceAsc(category);
        Product maxPriceProduct = productRepository.findTop1ByCategoryOrderByPriceDesc(category);

        return CategoryMinMaxPriceResponse.builder()
                .categoryName(categoryName)
                .minPrice(new CategoryMinMaxPrice(minPriceProduct.getBrand().getName(), minPriceProduct.getPrice()))
                .maxPrice(new CategoryMinMaxPrice(maxPriceProduct.getBrand().getName(), maxPriceProduct.getPrice()))
                .build();
    }

    public ProductDtoResponse createProduct(String productName, long price, long categoryId, long brandId) {
        Product product = Product.builder()
                .name(productName)
                .price(price)
                .category(categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CategoryNotFoundException(categoryId)))
                .brand(brandRepository.findById(brandId)
                        .orElseThrow(() -> new BrandNotFoundException(brandId)))
                .build();

        productRepository.save(product);

        return new ProductDtoResponse(product);
    }

    public ProductDtoResponse updateProduct(long productId, String productName, long price, long categoryId, long brandId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        product.updateName(productName);
        product.updatePrice(price);
        product.updateCategory(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId)));
        product.updateBrand(brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId)));

        productRepository.save(product);

        return new ProductDtoResponse(product);
    }

    @Transactional
    public ProductDeleteResponse deleteProduct(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);

        // 존재하지 않는 상품 ID 확인
        List<Long> foundProductIds = products.stream()
                .map(Product::getId)
                .toList();
        List<Long> missingIds = productIds.stream()
                .filter(id -> !foundProductIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            log.debug(String.format(ResponseCode.PRODUCT_NOT_FOUND.message, missingIds));
        }

        productRepository.deleteAllByIds(foundProductIds);

        return ProductDeleteResponse.builder()
                .successProductIds(foundProductIds)
                .failProductIds(missingIds)
                .build();
    }

    public List<ProductDtoResponse> selectAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductDtoResponse::new)
                .collect(Collectors.toList());
    }
}
