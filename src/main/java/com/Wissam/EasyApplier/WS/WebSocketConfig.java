package com.Wissam.EasyApplier.WS;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Value("${backend.host.url}")
  private String backendHostUrl;

  @Value("${frontend.host.url}")
  private String frontendHostUrl;

  @Value("${local.host.url}")
  private String localHostUrl;

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    List<String> allowedOrigins = List.of(localHostUrl, frontendHostUrl, backendHostUrl).stream()
        .filter(origin -> origin != null && !origin.isBlank())
        .collect(Collectors.toList());
    registry.addEndpoint("/ws").setAllowedOrigins(allowedOrigins.toArray(String[]::new)).withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic");
    registry.setApplicationDestinationPrefixes("/app");
  }

}
