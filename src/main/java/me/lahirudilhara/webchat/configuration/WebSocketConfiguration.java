package me.lahirudilhara.webchat.configuration;

import me.lahirudilhara.webchat.jwt.JwtService;
import me.lahirudilhara.webchat.jwt.JwtUserDetailsService;
import me.lahirudilhara.webchat.websocket.lib.transport.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    private final WebSocketHandler webSocketHandler;
    private final JwtService jwtService;
    private final JwtUserDetailsService  jwtUserDetailsService;

    public WebSocketConfiguration(WebSocketHandler webSocketHandler, JwtService jwtService, JwtUserDetailsService jwtUserDetailsService) {
        this.webSocketHandler = webSocketHandler;
        this.jwtService = jwtService;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // open websocket connection listener at /chat
        registry.addHandler(webSocketHandler, "ws/chat").addInterceptors(new WCJWTHandshakeInterceptor(jwtService,jwtUserDetailsService)).setAllowedOrigins("*");
    }
}
