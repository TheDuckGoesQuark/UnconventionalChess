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
        // i.e. methods annotated with @MessageMapping will only be triggered
        // if the client prefixes their request destination with this string
        config.setApplicationDestinationPrefixes("/app");

        // creates in-memory broker for sending messages to clients
        // i.e. subscribable topics must have this prefix
        config.enableSimpleBroker("/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // registers '/ws' as a STOMP endpoint
        // and adds SockJS as a fallback for when WebSockets are not supported.
        // This is the URL that will be used to perform the initial handshake for STOMP comms
        registry.addEndpoint("/ws").withSockJS();
    }
}
