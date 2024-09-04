package com.musinsa.style.controller.view;

import com.musinsa.style.controller.dto.brand.BrandDtoResponse;
import com.musinsa.style.controller.dto.product.ProductDtoResponse;
import com.musinsa.style.repository.entity.Category;
import com.musinsa.style.service.BrandService;
import com.musinsa.style.service.CategoryService;
import com.musinsa.style.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/musinsa")
@RequiredArgsConstructor
public class ViewController {

    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;

    @GetMapping(value = "/main")
    public String goMain(Model model) {
        // 전체 카테고리 조회
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);

        // 모델에 API 호출 결과 추가
        model.addAttribute("categoryMinPrices", productService.getCategoryMinPrices());
        model.addAttribute("brandMinTotalPrice", productService.getBrandMinTotalPrice());
        model.addAttribute("categoryMinMaxPrice", productService.getCategoryMinMaxPrice("상의"));

        return "main";  // main.html 템플릿으로 렌더링
    }

    @GetMapping(value = "/admin")
    public String goAdmin(Model model) {
        return "admin/admin";
    }

    @GetMapping(value = "/admin/brands")
    public String goAdminBrands(Model model) {
        List<BrandDtoResponse> brands = brandService.findAllBrands();
        model.addAttribute("brands", brands);
        return "admin/brands";
    }

    @GetMapping(value = "/admin/products")
    public String goAdminProducts(Model model) {
        List<ProductDtoResponse> products = productService.selectAllProducts();
        List<Category> categories = categoryService.findAllCategories();
        List<BrandDtoResponse> brands = brandService.findAllBrands();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        return "admin/products";
    }

}
