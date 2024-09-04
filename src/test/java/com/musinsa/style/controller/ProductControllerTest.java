package com.musinsa.style.controller;

import com.musinsa.style.enums.ResponseCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 고객은_카테고리_별로_최저가인_브랜드와_가격을_조회하고_총액을_확인할_수_있다() throws Exception {
        mockMvc.perform(get("/v1/products/categories/min-prices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.minPrice.categories", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '상의')].brandName", hasItem("C")))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '상의')].price", hasItem(10000)))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '아우터')].brandName", hasItem("E")))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '아우터')].price", hasItem(5000)))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '바지')].brandName", hasItem("D")))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '바지')].price", hasItem(3000)))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '스니커즈')].brandName", hasItem("G")))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '스니커즈')].price", hasItem(9000)))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '가방')].brandName", hasItem("A")))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '가방')].price", hasItem(2000)))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '모자')].brandName", hasItem("D")))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '모자')].price", hasItem(1500)))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '양말')].brandName", hasItem("I")))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '양말')].price", hasItem(1700)))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '액세서리')].brandName", hasItem("F")))
                .andExpect(jsonPath("$.data.minPrice.categories[?(@.categoryName == '액세서리')].price", hasItem(1900)))
                .andExpect(jsonPath("$.data.minPrice.totalPrice", is(34100)));
    }

    @Test
    void 고객은_단일_브랜드로_전체_카테고리_상품을_구매시_최저가인_브랜드와_총액을_확인할_수_있다() throws Exception {
        mockMvc.perform(get("/v1/products/brands/min-total-price")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.minPrice.brandName").isNotEmpty())
                .andExpect(jsonPath("$.data.minPrice.categories", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data.minPrice.totalPrice").isNumber());
    }

    @Test
    void 고객은_카테고리이름으로_최저가_최고가_브랜드와_상품가격을_확인할_수_있다() throws Exception {
        mockMvc.perform(get("/v1/products/categories/상의/min-max-price")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.categoryName", is("상의")))
                .andExpect(jsonPath("$.data.minPrice.brandName", is("C")))
                .andExpect(jsonPath("$.data.minPrice.price", is(10000)))
                .andExpect(jsonPath("$.data.maxPrice.brandName", is("I")))
                .andExpect(jsonPath("$.data.maxPrice.price", is(11400)));
    }

    @Test
    void 존재하지_않는_카테고리인_경우_실패값과_실패사유를_확인할_수_있다() throws Exception {
        String categoryName = "없는카테고리";
        mockMvc.perform(get(String.format("/v1/products/categories/%s/min-max-price", categoryName))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result", is(ResponseCode.CATEGORY_NOT_FOUND.result)))
                .andExpect(jsonPath("$.message", is(String.format(ResponseCode.CATEGORY_NOT_FOUND.message, categoryName))))
                .andExpect(status().isInternalServerError());
    }
}