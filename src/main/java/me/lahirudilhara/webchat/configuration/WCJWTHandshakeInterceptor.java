package me.lahirudilhara.webchat.configuration;

import me.lahirudilhara.webchat.jwt.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WCJWTHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtService jwtTokenUtil; // your JWT helper class
    private final UserDetailsService userDetailsService;

    public WCJWTHandshakeInterceptor(JwtService jwtTokenUtil, UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String query = request.getURI().getQuery();
        if(query == null || !query.startsWith("Authorization=")){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        String token = query.substring("Authorization=Bearer ".length());
        try{
            String username = jwtTokenUtil.extractUserName(token);
            if(username == null){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(!jwtTokenUtil.validateToken(token,userDetails)){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }
            attributes.put("user",userDetails);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            return true;
        }
        catch (Exception e){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
