package com.dartsapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // Allow your Netlify URL and your ngrok URL
        config.addAllowedOrigin("https://inspiring-mooncake-b7e422.netlify.app");
        config.addAllowedOrigin("https://1179-89-243-126-132.ngrok-free.app");
        config.addAllowedOrigin("https://1179-89-243-126-132.ngrok-free.app/api/users");
        config.addAllowedOrigin("https://preeminent-sfogliatella-bb63cf.netlify.app");
        config.addAllowedOrigin("http://localhost:5173");   
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply CORS configuration to all endpoints
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
