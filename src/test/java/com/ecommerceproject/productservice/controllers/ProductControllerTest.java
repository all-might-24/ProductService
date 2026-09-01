package com.ecommerceproject.productservice.controllers;

import com.ecommerceproject.productservice.dtos.*;
import com.ecommerceproject.productservice.services.ProductService;
import com.ecommerceproject.productservice.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean(name = "productStorageService")
    private ProductService productService;

    @MockitoBean
    private TokenService tokenService;


    @Test
    void contextLoads() {
    }


    // =========================================================
    // CREATE PRODUCT
    // =========================================================

    @Test
    void createProduct_shouldReturn201_whenRequestIsValid() throws Exception {

        CreateProductRequestDto requestDto = createValidProductRequest();

        CreateProductResponseDto responseDto = new CreateProductResponseDto();

        when(productService.createProduct(any(CreateProductRequestDto.class))).
                thenReturn(responseDto);

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isCreated());

        verify(productService)
                .createProduct(any(CreateProductRequestDto.class));
    }


    @Test
    void createProduct_shouldReturn400_whenTitleIsBlank() throws Exception {

        CreateProductRequestDto requestDto = createValidProductRequest();

        requestDto.setTitle("");

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .createProduct(any());
    }


    @Test
    void createProduct_shouldReturn400_whenTitleIsNull() throws Exception {

        CreateProductRequestDto requestDto = createValidProductRequest();

        requestDto.setTitle(null);

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)
                                )
                )
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .createProduct(any());
    }


    @Test
    void createProduct_shouldReturn400_whenPriceIsNull() throws Exception {

        CreateProductRequestDto requestDto = createValidProductRequest();

        requestDto.setPrice(null);

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .createProduct(any());
    }


    @Test
    void createProduct_shouldReturn400_whenPriceIsNegative() throws Exception {

        CreateProductRequestDto requestDto = createValidProductRequest();

        requestDto.setPrice(new BigDecimal("-100.00"));

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .createProduct(any());
    }


    @Test
    void createProduct_shouldReturn400_whenQuantityIsNegative() throws Exception {

        CreateProductRequestDto requestDto = createValidProductRequest();

        requestDto.setQty(-1);

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .createProduct(any());
    }


    @Test
    void createProduct_shouldReturn400_whenCategoryIdIsNull() throws Exception {

        CreateProductRequestDto requestDto = createValidProductRequest();

        requestDto.setCategoryId(null);

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .createProduct(any());
    }


    @Test
    void createProduct_shouldReturn400_whenJsonIsMalformed() throws Exception {

        String invalidJson = """
                {
                    "title": "Laptop",
                    "price": 1000,
                }
                """;

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson)
                )
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .createProduct(any());
    }


    // =========================================================
    // GET PRODUCT BY ID
    // =========================================================

    @Test
    void getProductById_shouldReturn200() throws Exception {

        GetProductResponseDto responseDto = new GetProductResponseDto();

        when(productService.getProductById(1L))
                .thenReturn(responseDto);

        mockMvc.perform(get("/products/{product_id}", 1L))
                .andExpect(status().isOk());

        verify(productService)
                .getProductById(1L);
    }


    @Test
    void getProductById_shouldReturn400_whenIdIsInvalidType() throws Exception {

        mockMvc.perform(get("/products/{product_id}", "abc"))
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .getProductById(anyLong());
    }


    // =========================================================
    // GET ALL PRODUCTS
    // =========================================================

    @Test
    void getAllProducts_shouldReturn200() throws Exception {

        when(productService.getAllProducts(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(null);

        mockMvc.perform(get("/products")).andExpect(status().isOk());

        verify(productService)
                .getAllProducts(
                        isNull(),
                        isNull(),
                        isNull(),
                        isNull(),
                        any(Pageable.class)
                );
    }


    @Test
    void getAllProducts_shouldPassSearchParameter() throws Exception {

        when(productService.getAllProducts(
                eq("Laptop"),
                isNull(),
                isNull(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(null);

        mockMvc.perform(get("/products").param("search", "Laptop"))
                .andExpect(status().isOk());

        verify(productService)
                .getAllProducts(
                        eq("Laptop"),
                        isNull(),
                        isNull(),
                        isNull(),
                        any(Pageable.class)
                );
    }


    @Test
    void getAllProducts_shouldPassCategoryFilter() throws Exception {

        when(productService.getAllProducts(
                isNull(),
                eq(1L),
                isNull(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(null);

        mockMvc.perform(get("/products").param("categoryId", "1")).andExpect(status().isOk());

        verify(productService)
                .getAllProducts(
                        isNull(),
                        eq(1L),
                        isNull(),
                        isNull(),
                        any(Pageable.class)
                );
    }


    @Test
    void getAllProducts_shouldPassMinimumPriceFilter() throws Exception {

        BigDecimal minPrice = new BigDecimal("500.00");

        when(productService.getAllProducts(
                isNull(),
                isNull(),
                eq(minPrice),
                isNull(),
                any(Pageable.class)
        )).thenReturn(null);

        mockMvc.perform(get("/products").param("minPrice", "500.00"))
                .andExpect(status().isOk());

        verify(productService)
                .getAllProducts(
                        isNull(),
                        isNull(),
                        eq(minPrice),
                        isNull(),
                        any(Pageable.class)
                );
    }


    @Test
    void getAllProducts_shouldPassMaximumPriceFilter() throws Exception {

        BigDecimal maxPrice = new BigDecimal("2000.00");

        when(productService.getAllProducts(
                isNull(),
                isNull(),
                isNull(),
                eq(maxPrice),
                any(Pageable.class)
        )).thenReturn(null);

        mockMvc.perform(get("/products").param("maxPrice", "2000.00"))
                .andExpect(status().isOk());

        verify(productService)
                .getAllProducts(
                        isNull(),
                        isNull(),
                        isNull(),
                        eq(maxPrice),
                        any(Pageable.class)
                );
    }


    @Test
    void getAllProducts_shouldPassCombinedFilters()
            throws Exception {

        BigDecimal minPrice = new BigDecimal("500.00");

        BigDecimal maxPrice = new BigDecimal("2000.00");

        when(productService.getAllProducts(
                eq("Laptop"),
                eq(1L),
                eq(minPrice),
                eq(maxPrice),
                any(Pageable.class)
        )).thenReturn(null);

        mockMvc.perform(get("/products")
                                .param("search", "Laptop")
                                .param("categoryId", "1")
                                .param("minPrice", "500.00")
                                .param("maxPrice", "2000.00")
                )
                .andExpect(status().isOk());

        verify(productService)
                .getAllProducts(
                        eq("Laptop"),
                        eq(1L),
                        eq(minPrice),
                        eq(maxPrice),
                        any(Pageable.class)
                );
    }


    @Test
    void getAllProducts_shouldSupportPagination()
            throws Exception {

        when(productService.getAllProducts(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(null);

        mockMvc.perform(
                        get("/products")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk());

        verify(productService)
                .getAllProducts(
                        isNull(),
                        isNull(),
                        isNull(),
                        isNull(),
                        argThat(
                                pageable ->
                                        pageable.getPageNumber() == 0
                                                &&
                                                pageable.getPageSize() == 10
                        )
                );
    }


    @Test
    void getAllProducts_shouldReturn400_whenMinPriceIsNegative()
            throws Exception {

        mockMvc.perform(
                        get("/products")
                                .param("minPrice", "-100")
                )
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .getAllProducts(
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                );
    }


    @Test
    void getAllProducts_shouldReturn400_whenMaxPriceIsLessThanMinPrice()
            throws Exception {

        mockMvc.perform(
                        get("/products")
                                .param("minPrice", "1000")
                                .param("maxPrice", "500")
                )
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .getAllProducts(
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                );
    }


    // =========================================================
    // PUT PRODUCT
    // =========================================================

    @Test
    void updateProduct_shouldReturn204_whenRequestIsValid()
            throws Exception {

        UpdateProductRequestDto requestDto = createValidUpdateRequest();

        doNothing().when(productService)
                .updateProductById(any(UpdateProductRequestDto.class), eq(1L));

        mockMvc.perform(
                        put("/products/{product_id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNoContent());

        verify(productService)
                .updateProductById(any(UpdateProductRequestDto.class), eq(1L));
    }


    @Test
    void updateProduct_shouldReturn400_whenTitleIsBlank() throws Exception {

        UpdateProductRequestDto requestDto = createValidUpdateRequest();

        requestDto.setTitle("");

        mockMvc.perform(put("/products/{product_id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verify(productService, never()).updateProductById(any(), anyLong());
    }


    @Test
    void updateProduct_shouldReturn400_whenPriceIsNegative() throws Exception {

        UpdateProductRequestDto requestDto = createValidUpdateRequest();

        requestDto.setPrice(new BigDecimal("-1"));

        mockMvc.perform(put("/products/{product_id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .updateProductById(any(), anyLong());
    }


    // =========================================================
    // PATCH PRODUCT
    // =========================================================

    @Test
    void patchProduct_shouldReturn204_whenRequestIsValid() throws Exception {

        PatchProductRequestDto requestDto = new PatchProductRequestDto();

        requestDto.setTitle("Updated Gaming Laptop");

        doNothing()
                .when(productService)
                .updateProductFieldsById(any(PatchProductRequestDto.class), eq(1L));

        mockMvc.perform(
                        patch("/products/{product_id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isNoContent());

        verify(productService)
                .updateProductFieldsById(any(PatchProductRequestDto.class), eq(1L));
    }


    @Test
    void patchProduct_shouldReturn400_whenPriceIsNegative() throws Exception {

        PatchProductRequestDto requestDto = new PatchProductRequestDto();

        requestDto.setPrice(new BigDecimal("-100"));

        mockMvc.perform(
                        patch("/products/{product_id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .updateProductFieldsById(any(), anyLong());
    }


    @Test
    void patchProduct_shouldAllowPartialRequest() throws Exception {

        PatchProductRequestDto requestDto = new PatchProductRequestDto();

        requestDto.setQty(20);

        mockMvc.perform(
                        patch("/products/{product_id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isNoContent());

        verify(productService)
                .updateProductFieldsById(any(PatchProductRequestDto.class), eq(1L));
    }


    // =========================================================
    // SOFT DELETE
    // =========================================================

    @Test
    void softDeleteProduct_shouldReturn204() throws Exception {

        doNothing()
                .when(productService)
                .softDeleteProductById(1L);

        mockMvc.perform(
                        delete("/products/{product_id}", 1L)
                )
                .andExpect(status().isNoContent());

        verify(productService)
                .softDeleteProductById(1L);
    }


    // =========================================================
    // HARD DELETE
    // =========================================================

    @Test
    void hardDeleteProduct_shouldReturn204() throws Exception {

        doNothing()
                .when(productService)
                .deleteProductById(1L);

        mockMvc.perform(delete("/products/{product_id}/hard-delete", 1L))
                .andExpect(status().isNoContent());

        verify(productService)
                .deleteProductById(1L);
    }


    // =========================================================
    // HELPER METHODS
    // =========================================================

    private CreateProductRequestDto createValidProductRequest() {

        CreateProductRequestDto requestDto = new CreateProductRequestDto();

        requestDto.setTitle("Gaming Laptop");
        requestDto.setDescription("High performance gaming laptop");
        requestDto.setPrice(new BigDecimal("1500.00"));
        requestDto.setQty(10);
        requestDto.setImageUrl("https://example.com/laptop.jpg");
        requestDto.setCategoryId(1L);

        return requestDto;
    }


    private UpdateProductRequestDto createValidUpdateRequest() {

        UpdateProductRequestDto requestDto = new UpdateProductRequestDto();

        requestDto.setTitle("Updated Gaming Laptop");
        requestDto.setDescription("Updated description");
        requestDto.setPrice(new BigDecimal("1800.00"));
        requestDto.setQty(15);
        requestDto.setImageUrl("https://example.com/updated-laptop.jpg");
        requestDto.setCategoryId(1L);

        return requestDto;
    }
}