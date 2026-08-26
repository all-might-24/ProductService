package com.ecommerceproject.productservice.services;

import io.jsonwebtoken.Claims;

public interface TokenService {
    boolean validateToken(String token);
    Claims extractClaims(String token);
}
