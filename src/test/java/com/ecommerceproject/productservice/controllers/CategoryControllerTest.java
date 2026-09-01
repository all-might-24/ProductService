package com.ecommerceproject.productservice.controllers;

import com.ecommerceproject.productservice.dtos.CreateCategoryRequestDto;
import com.ecommerceproject.productservice.dtos.CreateCategoryResponseDto;
import com.ecommerceproject.productservice.services.ICategoryService;
import com.ecommerceproject.productservice.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ICategoryService categoryService;

    @MockitoBean
    private TokenService tokenService;


    @Test
    void createCategory_withValidRequest_shouldReturn201() throws Exception {

        CreateCategoryRequestDto request =
                createRequest(
                        "Electronics",
                        "Electronic products"
                );

        CreateCategoryResponseDto response =
                new CreateCategoryResponseDto();

        response.setId(1L);
        response.setTitle("Electronics");
        response.setCreatedAt(
                LocalDateTime.of(
                        2026,
                        9,
                        1,
                        10,
                        0
                )
        );

        when(categoryService.createCategory(
                any(CreateCategoryRequestDto.class)
        )).thenReturn(response);

        mockMvc.perform(
                        post("/category")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Electronics"));

        verify(categoryService)
                .createCategory(
                        any(CreateCategoryRequestDto.class)
                );
    }


    @Test
    void createCategory_withNullTitle_shouldReturn400() throws Exception {

        CreateCategoryRequestDto request =
                createRequest(
                        null,
                        "Electronic products"
                );

        mockMvc.perform(
                        post("/category")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest());

        verify(categoryService, never())
                .createCategory(any());
    }


    @Test
    void createCategory_withBlankTitle_shouldReturn400() throws Exception {

        CreateCategoryRequestDto request =
                createRequest(
                        "   ",
                        "Electronic products"
                );

        mockMvc.perform(
                        post("/category")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest());

        verify(categoryService, never())
                .createCategory(any());
    }


    @Test
    void createCategory_withNullDescription_shouldReturn400() throws Exception {

        CreateCategoryRequestDto request =
                createRequest(
                        "Electronics",
                        null
                );

        mockMvc.perform(
                        post("/category")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest());

        verify(categoryService, never())
                .createCategory(any());
    }


    @Test
    void createCategory_withBlankDescription_shouldReturn400() throws Exception {

        CreateCategoryRequestDto request =
                createRequest(
                        "Electronics",
                        "   "
                );

        mockMvc.perform(
                        post("/category")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest());

        verify(categoryService, never())
                .createCategory(any());
    }


    @Test
    void createCategory_withEmptyJson_shouldReturn400() throws Exception {

        mockMvc.perform(
                        post("/category")
                                .contentType("application/json")
                                .content("{}")
                )
                .andExpect(status().isBadRequest());

        verify(categoryService, never())
                .createCategory(any());
    }


    @Test
    void createCategory_withMalformedJson_shouldReturn400() throws Exception {

        mockMvc.perform(
                        post("/category")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "title": "Electronics",
                                          "description":
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verify(categoryService, never())
                .createCategory(any());
    }


    private CreateCategoryRequestDto createRequest(
            String title,
            String description) {

        CreateCategoryRequestDto request =
                new CreateCategoryRequestDto();

        request.setTitle(title);
        request.setDescription(description);

        return request;
    }
}