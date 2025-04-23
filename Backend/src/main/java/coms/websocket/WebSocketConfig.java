package coms.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import coms.websocket.*;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory message broker to carry the messages back to the client on destinations prefixed with "/topic"
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");  // Prefix for messages sent to the server
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register the "/game" endpoint for WebSocket connections with SockJS fallback support
        registry.addEndpoint("/game").withSockJS();
    }
}
