package me.lahirudilhara.webchat.service.websocket.messageHandlers;

import me.lahirudilhara.webchat.dto.websocket.user.UserTextMessageDto;
import me.lahirudilhara.webchat.websocket.UserMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserTextMessageHandler implements UserMessageHandler<UserTextMessageDto> {
    private static final Logger log = LoggerFactory.getLogger(UserTextMessageHandler.class);

    @Override
    public Class<UserTextMessageDto> getMessageType() {
        return UserTextMessageDto.class;
    }

    @Override
    public void handleMessage(UserTextMessageDto message) {
        System.out.println("UserTextMessageHandler.handleMessage");
        log.debug(message.toString());
    }
}
