package me.lahirudilhara.webchat.websocket.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.common.util.JsonUtil;
import me.lahirudilhara.webchat.websocket.events.ClientExceptionEvent;
import me.lahirudilhara.webchat.websocket.events.MulticastDataEvent;
import me.lahirudilhara.webchat.websocket.events.UnicastDataEvent;
import me.lahirudilhara.webchat.websocket.session.SessionManager;
import me.lahirudilhara.webchat.websocket.session.WebSocketUserSession;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class MessageListener {

    private final SessionManager sessionManager;

    public MessageListener(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    private void sendTextMessage(WebSocketSession session, String message) {
        try{
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void sendtextrMessage(WebSocketSession session, Object data){
        try{
            session.sendMessage(new TextMessage(JsonUtil.objectToJson(data)));
        }
        catch (JsonProcessingException e){
            log.error("The message json is not in correct format. The message is {}",e.getMessage());
        } catch (IOException e) {
            log.error("An IO exception occurred. The message is {}",e.getMessage());
        }
        catch (Exception e){
            log.error("An exception occurred. The message is {}",e.getMessage());
        }

    }

    @Async
    @EventListener
    public void OnClientExceptionEvent(ClientExceptionEvent event){
        WebSocketUserSession webSocketUserSession = sessionManager.getUserByUsername(event.username());
        if(webSocketUserSession == null) return;
        WebSocketSession session = webSocketUserSession.getSession();
        if(!session.isOpen()) {
            log.error("Session has been closed for the user {}",event.username());
            return;
        }
        sendTextMessage(session, event.message());
    }

    @Async
    @EventListener
    public void OnMulticastDataEvent(MulticastDataEvent event){
        List<WebSocketUserSession> webSocketUserSessions = sessionManager.getUsersByUsernames(event.users());
        for(WebSocketUserSession webSocketUserSession : webSocketUserSessions){
            if(webSocketUserSession == null) continue;
            WebSocketSession session = webSocketUserSession.getSession();
            if(!session.isOpen()) {
                log.error("Session has been closed for the user {}",event.users());
                continue;
            }
            sendtextrMessage(session, event.data());
        }
    }

    @Async
    @EventListener
    public void OnUnicastDataEvent(UnicastDataEvent event){
        WebSocketUserSession webSocketUserSession = sessionManager.getUserByUsername(event.username());
        if(webSocketUserSession == null) return;
        WebSocketSession session = webSocketUserSession.getSession();
        if(!session.isOpen()) {
            log.error("Session has been closed for the user {}",event.username());
            return;
        }
        sendtextrMessage(session,event.object());
    }
}
