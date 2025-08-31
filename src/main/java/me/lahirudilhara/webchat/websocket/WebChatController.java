package me.lahirudilhara.webchat.websocket;

import me.lahirudilhara.webchat.websocket.events.OnClientMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WebChatController {
    private static final Logger log = LoggerFactory.getLogger(WebChatController.class);

    public WebChatController() {
    }

    @EventListener
    public void onJoinMessage(OnClientMessageEvent event){
        System.out.println("The data is "+event.getMessageDTO().getRoomId());
    }

}
