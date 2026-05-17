package com.myproject.retail.billingsoftware.config;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.myproject.retail.billingsoftware.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    @Value("${cors.allowed.origins:http://localhost:5173}")
    private String corsOriginsRaw;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth

                // Public endpoints (no token required)
                .requestMatchers(
                    "/api/v1.0/auth/login",
                    "/api/v1.0/auth/register",
                    "/auth/login",
                    "/auth/register"
          
                ).permitAll()

                // Static file access
                .requestMatchers(
                    "/uploads/**",
                    "/api/v1.0/uploads/**"
                ).permitAll()

                // Super Admin only — managing admins
                .requestMatchers(
                    "/api/v1.0/superadmin/**",
                    "/superadmin/**"
                ).hasRole("SUPER_ADMIN")

                // Admin + Super Admin — managing items, categories, users
                .requestMatchers(
                    "/api/v1.0/admin/**",
                    "/admin/**"
                ).hasAnyRole("ADMIN", "SUPER_ADMIN")

                // All authenticated roles — billing, viewing data
                .requestMatchers(
                    "/api/v1.0/categories/**",
                    "/api/v1.0/items/**",
                    "/api/v1.0/orders/**",
                    "/api/v1.0/payments/**",
                    "/api/v1.0/dashboard/**",
                    "/categories/**",
                    "/items/**",
                    "/orders/**",
                    "/payments/**",
                    "/dashboard/**"
                ).hasAnyRole("ADMIN", "USER", "SUPER_ADMIN")

                // Everything else requires authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList(corsOriginsRaw.split(",")));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
   
}