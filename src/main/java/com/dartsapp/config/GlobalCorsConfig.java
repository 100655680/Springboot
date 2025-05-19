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
        config.addAllowedOrigin("https://briefing-tion-saved-nb.trycloudflare.com");
        config.addAllowedOrigin("https://briefing-tion-saved-nb.trycloudflare.com/api/users");
        config.addAllowedOrigin("https://preeminent-sfogliatella-bb63cf.netlify.app");
        config.addAllowedOrigin("http://localhost:5173");   
        config.addAllowedOrigin("http://192.168.4.20:5173");       
        config.addAllowedOrigin("http://192.168.4.20:8080");  
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply CORS configuration to all endpoints
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
