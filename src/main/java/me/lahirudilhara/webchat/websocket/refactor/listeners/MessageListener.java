package me.lahirudilhara.webchat.websocket.refactor.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.common.util.JsonUtil;
import me.lahirudilhara.webchat.websocket.refactor.events.ClientErrorEvent;
import me.lahirudilhara.webchat.websocket.refactor.events.MulticastDataEvent;
import me.lahirudilhara.webchat.websocket.refactor.events.UnicastDataEvent;
import me.lahirudilhara.webchat.websocket.refactor.session.SessionManager;
import me.lahirudilhara.webchat.websocket.refactor.session.WebSocketUserSession;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Slf4j
public class MessageListener {

    private final SessionManager sessionManager;

    public MessageListener(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    private void sendAsTextMessage(List<WebSocketUserSession> webSocketUserSessions, Object data){
        if(webSocketUserSessions.isEmpty()) return;
        webSocketUserSessions.forEach(webSocketUserSession -> {
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
        });
    }


    public void OnClientErrorEvent(ClientErrorEvent event){
        List<WebSocketUserSession> webSocketUserSession = sessionManager.getUserSessions(event.username());
        WebSocketUserSession errorSession = webSocketUserSession.stream().filter(s->s.getSession().getId().equals(event.sessionId())).findFirst().orElse(null);
        if(errorSession==null) return;
        sendAsTextMessage(List.of(errorSession),event.error());
    }

    public void OnMulticastDataEvent(MulticastDataEvent event){
        List<WebSocketUserSession> webSocketUserSessions = sessionManager.getMultipleUserSessions(event.users());
        sendAsTextMessage(webSocketUserSessions,event.data());
    }

    @Async
    @EventListener
    public void OnUnicastDataEvent(UnicastDataEvent event){
        List<WebSocketUserSession> webSocketUserSession = sessionManager.getUserSessions(event.username());
        sendAsTextMessage(webSocketUserSession,event.object());
    }
}
