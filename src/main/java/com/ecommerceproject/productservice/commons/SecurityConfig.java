package com.ecommerceproject.productservice.commons;

import com.ecommerceproject.productservice.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
            httpSecurity
                    .csrf(csrf -> csrf.disable())
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PATCH, "/products/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
                            .anyRequest().permitAll()
                    )
                    .exceptionHandling(exception ->
                            exception
                                    .authenticationEntryPoint((request, response, authException)
                                            -> response.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
                                    .accessDeniedHandler((request, response, accessDeniedException)
                                            -> response.setStatus(HttpServletResponse.SC_FORBIDDEN))
                    )
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            return httpSecurity.build();
        }
}
