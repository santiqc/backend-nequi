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


        corsConfig.addAllowedOriginPattern("*");


        corsConfig.addAllowedMethod(HttpMethod.GET.name());
        corsConfig.addAllowedMethod(HttpMethod.POST.name());
        corsConfig.addAllowedMethod(HttpMethod.PUT.name());
        corsConfig.addAllowedMethod(HttpMethod.DELETE.name());
        corsConfig.addAllowedMethod(HttpMethod.OPTIONS.name());
        corsConfig.addAllowedMethod(HttpMethod.PATCH.name());


        corsConfig.addAllowedHeader("Authorization");
        corsConfig.addAllowedHeader("Content-Type");
        corsConfig.addAllowedHeader("Accept");
        corsConfig.addAllowedHeader("Origin");
        corsConfig.addAllowedHeader("Access-Control-Request-Method");
        corsConfig.addAllowedHeader("Access-Control-Request-Headers");


        corsConfig.addExposedHeader("Access-Control-Allow-Origin");
        corsConfig.addExposedHeader("Access-Control-Allow-Credentials");


        corsConfig.setAllowCredentials(true);


        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", corsConfig);
        source.registerCorsConfiguration("/v3/**", corsConfig);
        source.registerCorsConfiguration("/webjars/**", corsConfig);

        return new CorsWebFilter(source);
    }
}