package ChatService.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/chat");
        config.setUserDestinationPrefix("/chat");
        config.setApplicationDestinationPrefixes("/app");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
       registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3005").withSockJS();
        //registry.addEndpoint("/ws") // Endpoint pentru SockJS/WebSocket
             //   .setAllowedOrigins("*") // Permite conexiuni de oriunde (poți specifica domenii mai strict)
           //     .withSockJS();
     //   registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3005");
      //  registry.addEndpoint("/ws").setAllowedOriginPatterns("*");

    }
/*    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // Allow the frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }*/
}
