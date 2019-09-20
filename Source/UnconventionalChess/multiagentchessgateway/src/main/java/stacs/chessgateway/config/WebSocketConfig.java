package stacs.chessgateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * Configuration class for adding websocket support
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // set prefix for websocket endpoints to '/app'
        config.setApplicationDestinationPrefixes("/app");
        // creates in-memory broker for sending messsages to clients
        config.enableSimpleBroker("/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // registers '/chess' as a STOMP endpoint
        // and adds SockJS as a fallback for when WebSockets are not supported
        registry.addEndpoint("/ws").withSockJS();
    }
}
