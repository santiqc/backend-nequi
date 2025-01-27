package com.nequi.franquicias.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.addAllowedOriginPattern("https://*");
        corsConfig.addAllowedOriginPattern("http://localhost:*");

        corsConfig.addAllowedMethod("*");

        corsConfig.addAllowedHeader("*");

        corsConfig.addExposedHeader("Access-Control-Allow-Origin");
        corsConfig.addExposedHeader("Access-Control-Allow-Credentials");
        corsConfig.addExposedHeader("Access-Control-Allow-Methods");
        corsConfig.addExposedHeader("Access-Control-Allow-Headers");

        corsConfig.setAllowCredentials(true);

        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}