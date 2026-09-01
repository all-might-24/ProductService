package com.ecommerceproject.productservice.controllers;

import com.ecommerceproject.productservice.commons.SecurityConfig;
import com.ecommerceproject.productservice.dtos.CreateProductResponseDto;
import com.ecommerceproject.productservice.security.JwtAuthenticationFilter;
import com.ecommerceproject.productservice.services.ProductService;
import com.ecommerceproject.productservice.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import({
        SecurityConfig.class,
        JwtAuthenticationFilter.class,
        ProductControllerSecurityTest.TestSecurityConfiguration.class
})
class ProductControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean(name = "productStorageService")
    private ProductService productService;

    @MockitoBean
    private TokenService tokenService;


    @TestConfiguration
    @EnableWebSecurity
    static class TestSecurityConfiguration {
    }


/*
 * =========================================================
 * PUBLIC GET ENDPOINTS
 * =========================================================
 */

    @Test
    void getProductById_withoutAuthentication_shouldReturn200()
            throws Exception {

        mockMvc.perform(
                        get("/products/{product_id}", 1L)
                )
                .andExpect(status().isOk());
    }

    @Test
    void getAllProducts_withoutAuthentication_shouldReturn200()
            throws Exception {

        mockMvc.perform(
                        get("/products")
                )
                .andExpect(status().isOk());
    }


/*
 * =========================================================
 * CREATE
 * =========================================================
 */

    @Test
    void createProduct_withoutAuthentication_shouldReturn401() throws Exception {

        mockMvc.perform(
                        post("/products")
                                .contentType("application/json")
                                .content(validCreateProductJson())
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createProduct_withInvalidToken_shouldReturn401() throws Exception {

        when(tokenService.validateToken("invalid-token"))
                .thenReturn(false);

        mockMvc.perform(
                        post("/products")
                                .header(
                                        "Authorization",
                                        "Bearer invalid-token"
                                )
                                .contentType("application/json")
                                .content(validCreateProductJson())
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createProduct_withUserRole_shouldReturn403() throws Exception {

        mockToken(
                "user-token",
                1L,
                List.of("USER")
        );

        mockMvc.perform(
                        post("/products")
                                .header(
                                        "Authorization",
                                        "Bearer user-token"
                                )
                                .contentType("application/json")
                                .content(validCreateProductJson())
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void createProduct_withAdminRole_shouldReturn201() throws Exception {

        mockToken(
                "admin-token",
                1L,
                List.of("ADMIN")
        );

        when(productService.createProduct(any()))
                .thenReturn(
                        new CreateProductResponseDto()
                );

        mockMvc.perform(
                        post("/products")
                                .header(
                                        "Authorization",
                                        "Bearer admin-token"
                                )
                                .contentType("application/json")
                                .content(validCreateProductJson())
                )
                .andExpect(status().isCreated());
    }


/*
 * =========================================================
 * UPDATE
 * =========================================================
 */

    @Test
    void updateProduct_withoutAuthentication_shouldReturn401() throws Exception {

        mockMvc.perform(
                        put("/products/{product_id}", 1L)
                                .contentType("application/json")
                                .content(validUpdateProductJson())
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateProduct_withUserRole_shouldReturn403() throws Exception {

        mockToken(
                "user-token",
                1L,
                List.of("USER")
        );

        mockMvc.perform(
                        put("/products/{product_id}", 1L)
                                .header(
                                        "Authorization",
                                        "Bearer user-token"
                                )
                                .contentType("application/json")
                                .content(validUpdateProductJson())
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void updateProduct_withAdminRole_shouldReturn204() throws Exception {

        mockToken(
                "admin-token",
                1L,
                List.of("ADMIN")
        );

        mockMvc.perform(
                        put("/products/{product_id}", 1L)
                                .header(
                                        "Authorization",
                                        "Bearer admin-token"
                                )
                                .contentType("application/json")
                                .content(validUpdateProductJson())
                )
                .andExpect(status().isNoContent());
    }


/*
 * =========================================================
 * PATCH
 * =========================================================
 */

    @Test
    void patchProduct_withoutAuthentication_shouldReturn401() throws Exception {

        mockMvc.perform(
                        patch("/products/{product_id}", 1L)
                                .contentType("application/json")
                                .content("""
                                        {
                                          "title": "Updated Product"
                                        }
                                        """)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void patchProduct_withUserRole_shouldReturn403() throws Exception {

        mockToken(
                "user-token",
                1L,
                List.of("USER")
        );

        mockMvc.perform(
                        patch("/products/{product_id}", 1L)
                                .header(
                                        "Authorization",
                                        "Bearer user-token"
                                )
                                .contentType("application/json")
                                .content("""
                                        {
                                          "title": "Updated Product"
                                        }
                                        """)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void patchProduct_withAdminRole_shouldReturn204() throws Exception {

        mockToken(
                "admin-token",
                1L,
                List.of("ADMIN")
        );

        mockMvc.perform(
                        patch("/products/{product_id}", 1L)
                                .header(
                                        "Authorization",
                                        "Bearer admin-token"
                                )
                                .contentType("application/json")
                                .content("""
                                        {
                                          "title": "Updated Product"
                                        }
                                        """)
                )
                .andExpect(status().isNoContent());
    }


/*
 * =========================================================
 * SOFT DELETE
 * =========================================================
 */

    @Test
    void softDeleteProduct_withoutAuthentication_shouldReturn401() throws Exception {

        mockMvc.perform(
                        delete("/products/{product_id}", 1L)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void softDeleteProduct_withUserRole_shouldReturn403() throws Exception {

        mockToken(
                "user-token",
                1L,
                List.of("USER")
        );

        mockMvc.perform(
                        delete("/products/{product_id}", 1L)
                                .header(
                                        "Authorization",
                                        "Bearer user-token"
                                )
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void softDeleteProduct_withAdminRole_shouldReturn204() throws Exception {

        mockToken(
                "admin-token",
                1L,
                List.of("ADMIN")
        );

        mockMvc.perform(
                        delete("/products/{product_id}", 1L)
                                .header(
                                        "Authorization",
                                        "Bearer admin-token"
                                )
                )
                .andExpect(status().isNoContent());
    }


/*
 * =========================================================
 * HARD DELETE
 * =========================================================
 */

    @Test
    void hardDeleteProduct_withoutAuthentication_shouldReturn401() throws Exception {

        mockMvc.perform(
                        delete(
                                "/products/{product_id}/hard-delete",
                                1L
                        )
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void hardDeleteProduct_withUserRole_shouldReturn403() throws Exception {

        mockToken(
                "user-token",
                1L,
                List.of("USER")
        );

        mockMvc.perform(
                        delete(
                                "/products/{product_id}/hard-delete",
                                1L
                        )
                                .header(
                                        "Authorization",
                                        "Bearer user-token"
                                )
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void hardDeleteProduct_withAdminRole_shouldReturn204() throws Exception {

        mockToken(
                "admin-token",
                1L,
                List.of("ADMIN")
        );

        mockMvc.perform(
                        delete(
                                "/products/{product_id}/hard-delete",
                                1L
                        )
                                .header(
                                        "Authorization",
                                        "Bearer admin-token"
                                )
                )
                .andExpect(status().isNoContent());
    }


/*
 * =========================================================
 * AUTH HEADER EDGE CASE
 * =========================================================
 */

    @Test
    void createProduct_withNonBearerAuthorizationHeader_shouldReturn401() throws Exception {

        mockMvc.perform(
                        post("/products")
                                .header(
                                        "Authorization",
                                        "Basic abc123"
                                )
                                .contentType("application/json")
                                .content(validCreateProductJson())
                )
                .andExpect(status().isUnauthorized());
    }


/*
 * =========================================================
 * HELPERS
 * =========================================================
 */

    private void mockToken(
            String token,
            Long userId,
            List<String> roles) {

        Claims claims =
                mock(Claims.class);

        when(tokenService.validateToken(token))
                .thenReturn(true);

        when(tokenService.extractClaims(token))
                .thenReturn(claims);

        when(claims.get(
                "userId",
                Long.class
        )).thenReturn(userId);

        when(claims.get(
                "scope",
                List.class
        )).thenReturn(roles);
    }

    private String validCreateProductJson() {

        return """
                {
                  "title": "Gaming Laptop",
                  "description": "High performance laptop",
                  "price": 1500.00,
                  "qty": 10,
                  "imageUrl": "laptop.jpg",
                  "categoryId": 1
                }
                """;
    }

    private String validUpdateProductJson() {

        return """
                {
                  "title": "Updated Laptop",
                  "description": "Updated description",
                  "price": 1200.00,
                  "qty": 5,
                  "imageUrl": "updated.jpg",
                  "categoryId": 1
                }
                """;
    }
}