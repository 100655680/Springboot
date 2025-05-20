// src/main/java/com/dartsapp/config/WebSocketConfig.java
package com.dartsapp.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
          .addEndpoint("/ws")
          .setAllowedOriginPatterns("*")
          .addInterceptors(new HandshakeInterceptor() {
              @Override
              public boolean beforeHandshake(ServerHttpRequest request,
                                             ServerHttpResponse response,
                                             WebSocketHandler wsHandler,
                                             Map<String, Object> attributes) throws Exception {
                  if (request instanceof ServletServerHttpRequest) {
                      String user = ((ServletServerHttpRequest) request)
                                      .getServletRequest()
                                      .getParameter("username");
                      if (user != null && !user.isEmpty()) {
                          attributes.put("username", user);
                      }
                  }
                  return true;
              }
              @Override
              public void afterHandshake(ServerHttpRequest request,
                                         ServerHttpResponse response,
                                         WebSocketHandler wsHandler,
                                         Exception exception) {
              }
          })
          .setHandshakeHandler(new DefaultHandshakeHandler() {
              @Override
              protected Principal determineUser(ServerHttpRequest request,
                                                WebSocketHandler wsHandler,
                                                Map<String, Object> attributes) {
                  Object user = attributes.get("username");
                  if (user instanceof String) {
                      return () -> (String) user;
                  }
                  return super.determineUser(request, wsHandler, attributes);
              }
          });
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // messages from @MessageMapping go to /app/**
        registry.setApplicationDestinationPrefixes("/app");
        // enable simple in-memory /topic and /queue broker
        registry.enableSimpleBroker("/topic", "/queue");
        // enable /user/{username}/... destinations
        registry.setUserDestinationPrefix("/user");
    }
}
