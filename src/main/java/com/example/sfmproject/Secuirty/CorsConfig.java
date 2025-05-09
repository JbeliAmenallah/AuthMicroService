package com.example.sfmproject.Secuirty;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig implements CorsConfigurationSource {

    private CorsConfiguration commonConfig;

    @PostConstruct
    public void init() {
        commonConfig = new CorsConfiguration();
        commonConfig.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        commonConfig.addAllowedHeader("*");
        commonConfig.addAllowedMethod("*");
        commonConfig.setAllowCredentials(true);
    }

    @Bean
    public Filter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Register common CORS configuration for all relevant endpoints
        source.registerCorsConfiguration("/ws/**", commonConfig);
        source.registerCorsConfiguration("/api/**", commonConfig);
        source.registerCorsConfiguration("/Msg/**", commonConfig);
        source.registerCorsConfiguration("/**", commonConfig);

        return new CorsFilter(source);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", commonConfig);
        return source;
    }

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        return commonConfig;
    }
}