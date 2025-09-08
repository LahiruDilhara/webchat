package me.lahirudilhara.webchat.websocket.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.common.util.JsonUtil;
import me.lahirudilhara.webchat.websocket.events.ClientErrorEvent;
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

    private void sendAsTextMessage(WebSocketUserSession webSocketUserSession, Object data){
        if(webSocketUserSession == null) return;
        WebSocketSession session = webSocketUserSession.getSession();
        if(!session.isOpen()) {
            log.error("Session has been closed for the user {}",webSocketUserSession.getUsername());
            return;
        }
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
    public void OnClientErrorEvent(ClientErrorEvent event){
        System.out.println("Handling the error");
        WebSocketUserSession webSocketUserSession = sessionManager.getUserByUsername(event.username());
        sendAsTextMessage(webSocketUserSession,event.error());
    }

    @Async
    @EventListener
    public void OnMulticastDataEvent(MulticastDataEvent event){
        List<WebSocketUserSession> webSocketUserSessions = sessionManager.getUsersByUsernames(event.users());
        for(WebSocketUserSession webSocketUserSession : webSocketUserSessions){
            sendAsTextMessage(webSocketUserSession,event.data());
        }
    }

    @Async
    @EventListener
    public void OnUnicastDataEvent(UnicastDataEvent event){
        WebSocketUserSession webSocketUserSession = sessionManager.getUserByUsername(event.username());
        sendAsTextMessage(webSocketUserSession,event.object());
    }
}
