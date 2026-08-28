package com.ecommerceproject.productservice.controllers;

import com.ecommerceproject.productservice.commons.SecurityConfig;
import com.ecommerceproject.productservice.dtos.*;
import com.ecommerceproject.productservice.exceptions.ProductNotFoundException;
import com.ecommerceproject.productservice.services.ProductService;
import com.ecommerceproject.productservice.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean(name = "productStorageService")
    private ProductService productService;

    @MockitoBean
    private TokenService tokenService;


    @Test
    @WithMockUser(roles = "ADMIN")
    void createProduct_shouldReturn201() throws Exception {

        CreateProductRequestDto requestDto = new CreateProductRequestDto();

        requestDto.setTitle("Gaming Laptop");
        requestDto.setDescription("High performance gaming laptop");
        requestDto.setPrice(new BigDecimal("1500.00"));
        requestDto.setQty(10);
        requestDto.setImageUrl("laptop.jpg");
        requestDto.setCategoryId(1L);

        CreateProductResponseDto responseDto = new CreateProductResponseDto();

        when(productService.createProduct(any(CreateProductRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isCreated());

        verify(productService).createProduct(any(CreateProductRequestDto.class));
    }

    @Test
    void createProduct_withoutAuthentication_shouldReturn401() throws Exception {

        CreateProductRequestDto requestDto = new CreateProductRequestDto();

        requestDto.setTitle("Gaming Laptop");
        requestDto.setDescription("High performance gaming laptop");
        requestDto.setPrice(new BigDecimal("1500.00"));
        requestDto.setQty(10);
        requestDto.setImageUrl("laptop.jpg");
        requestDto.setCategoryId(1L);

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isUnauthorized());

        verify(productService, never())
                .createProduct(any(CreateProductRequestDto.class));
    }


    @Test
    @WithMockUser(roles = "USER")
    void createProduct_withUserRole_shouldReturn403() throws Exception {

        CreateProductRequestDto requestDto = new CreateProductRequestDto();

        requestDto.setTitle("Gaming Laptop");
        requestDto.setDescription("High performance gaming laptop");
        requestDto.setPrice(new BigDecimal("1500.00"));
        requestDto.setQty(10);
        requestDto.setImageUrl("laptop.jpg");
        requestDto.setCategoryId(1L);

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isForbidden());

        verify(productService, never())
                .createProduct(any(CreateProductRequestDto.class));
    }


    // ============================================================
    // GET /products/{id}
    // ============================================================

    @Test
    void getProductById_shouldReturn200() throws Exception {

        GetProductResponseDto responseDto =
                new GetProductResponseDto();

        responseDto.setId(1L);
        responseDto.setTitle("Gaming Laptop");

        when(productService.getProductById(1L))
                .thenReturn(responseDto);

        mockMvc.perform(
                        get("/products/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Gaming Laptop"));

        verify(productService)
                .getProductById(1L);
    }


    @Test
    void getProductById_whenProductDoesNotExist_shouldReturnExpectedError() throws Exception {

        when(productService.getProductById(999L))
                .thenThrow(
                        new ProductNotFoundException(
                                "Product not found with id : 999"
                        )
                );

        mockMvc.perform(
                        get("/products/999")
                )
                .andExpect(status().isNotFound());

        verify(productService)
                .getProductById(999L);
    }


    // ============================================================
    // GET /products
    // ============================================================

    @Test
    void getAllProducts_shouldReturn200() throws Exception {

        PageResponseDto<GetProductResponseDto> responseDto =
                new PageResponseDto<>();

        when(productService.getAllProducts(
                any(),
                any(),
                any(),
                any(),
                any()
        )).thenReturn(responseDto);

        mockMvc.perform(
                        get("/products")
                )
                .andExpect(status().isOk());

        verify(productService).getAllProducts(
                any(),
                any(),
                any(),
                any(),
                any()
        );
    }


    @Test
    void getAllProducts_withSearchAndPriceFilter_shouldReturn200()
            throws Exception {

        PageResponseDto<GetProductResponseDto> responseDto =
                new PageResponseDto<>();

        when(productService.getAllProducts(
                eq("laptop"),
                eq(1L),
                eq(new BigDecimal("500")),
                eq(new BigDecimal("2000")),
                any()
        )).thenReturn(responseDto);

        mockMvc.perform(
                        get("/products")
                                .param("search", "laptop")
                                .param("categoryId", "1")
                                .param("minPrice", "500")
                                .param("maxPrice", "2000")
                )
                .andExpect(status().isOk());

        verify(productService).getAllProducts(
                eq("laptop"),
                eq(1L),
                eq(new BigDecimal("500")),
                eq(new BigDecimal("2000")),
                any()
        );
    }


    // ============================================================
    // PUT /products/{id}
    // ============================================================

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProduct_shouldReturn204() throws Exception {

        UpdateProductRequestDto requestDto =
                new UpdateProductRequestDto();

        requestDto.setTitle("Updated Laptop");
        requestDto.setDescription("Updated description");
        requestDto.setPrice(new BigDecimal("1800.00"));
        requestDto.setQty(5);
        requestDto.setImageUrl("updated.jpg");
        requestDto.setCategoryId(1L);

        doNothing().when(productService)
                .updateProductById(any(UpdateProductRequestDto.class), eq(1L));

        mockMvc.perform(
                        put("/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isNoContent());

        verify(productService)
                .updateProductById(
                        any(UpdateProductRequestDto.class),
                        eq(1L)
                );
    }


    @Test
    void updateProduct_withoutAuthentication_shouldReturn401()
            throws Exception {

        UpdateProductRequestDto requestDto =
                new UpdateProductRequestDto();

        requestDto.setTitle("Updated Laptop");
        requestDto.setDescription("Updated description");
        requestDto.setPrice(new BigDecimal("1800.00"));
        requestDto.setQty(5);
        requestDto.setImageUrl("updated.jpg");
        requestDto.setCategoryId(1L);

        mockMvc.perform(
                        put("/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isUnauthorized());

        verify(productService, never())
                .updateProductById(
                        any(UpdateProductRequestDto.class),
                        anyLong()
                );
    }


    // ============================================================
    // PATCH /products/{id}
    // ============================================================

    @Test
    @WithMockUser(roles = "ADMIN")
    void patchProduct_shouldReturn204() throws Exception {

        PatchProductRequestDto requestDto =
                new PatchProductRequestDto();

        requestDto.setTitle("Patched Laptop");
        requestDto.setPrice(new BigDecimal("1700.00"));

        doNothing().when(productService)
                .updateProductFieldsById(
                        any(PatchProductRequestDto.class),
                        eq(1L)
                );

        mockMvc.perform(
                        patch("/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isNoContent());

        verify(productService)
                .updateProductFieldsById(
                        any(PatchProductRequestDto.class),
                        eq(1L)
                );
    }


    // ============================================================
    // HARD DELETE
    // ============================================================

    @Test
    @WithMockUser(roles = "ADMIN")
    void hardDeleteProduct_shouldReturn204() throws Exception {

        doNothing().when(productService)
                .deleteProductById(1L);

        mockMvc.perform(
                        delete("/products/1/hard-delete")
                )
                .andExpect(status().isNoContent());

        verify(productService)
                .deleteProductById(1L);
    }


    // ============================================================
    // SOFT DELETE
    // ============================================================

    @Test
    @WithMockUser(roles = "ADMIN")
    void softDeleteProduct_shouldReturn204() throws Exception {

        doNothing().when(productService)
                .softDeleteProductById(1L);

        mockMvc.perform(
                        delete("/products/1")
                )
                .andExpect(status().isNoContent());

        verify(productService)
                .softDeleteProductById(1L);
    }


    // ============================================================
    // DELETE WITHOUT ADMIN
    // ============================================================

    @Test
    @WithMockUser(roles = "USER")
    void deleteProduct_withUserRole_shouldReturn403() throws Exception {

        mockMvc.perform(
                        delete("/products/1")
                )
                .andExpect(status().isForbidden());

        verify(productService, never())
                .softDeleteProductById(anyLong());
    }


    @Test
    void deleteProduct_withoutAuthentication_shouldReturn401()
            throws Exception {

        mockMvc.perform(
                        delete("/products/1")
                )
                .andExpect(status().isUnauthorized());

        verify(productService, never())
                .softDeleteProductById(anyLong());
    }


}