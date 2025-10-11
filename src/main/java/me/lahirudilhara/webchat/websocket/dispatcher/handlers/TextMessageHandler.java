package me.lahirudilhara.webchat.websocket.dispatcher.handlers;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.websocket.dispatcher.MessageHandler;
import me.lahirudilhara.webchat.websocket.dto.requests.TextMessageDTO;
import me.lahirudilhara.webchat.websocket.interfaces.ClientErrorHandler;
import me.lahirudilhara.webchat.websocket.queues.TextMessageQueue;
import me.lahirudilhara.webchat.websocket.queues.events.TextMessageEvent;
import me.lahirudilhara.webchat.websocket.services.SessionRoomValidator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TextMessageHandler implements MessageHandler<TextMessageDTO> {
    private final SessionRoomValidator sessionRoomValidator;
    private final ClientErrorHandler clientErrorHandler;
    private final TextMessageQueue textMessageQueue;

    public TextMessageHandler(SessionRoomValidator sessionRoomValidator, ClientErrorHandler clientErrorHandler, TextMessageQueue textMessageQueue) {
        this.sessionRoomValidator = sessionRoomValidator;
        this.clientErrorHandler = clientErrorHandler;
        this.textMessageQueue = textMessageQueue;
    }

    @Override
    public Class<TextMessageDTO> getMessageClassType() {
        return TextMessageDTO.class;
    }

    @Override
    public void handleMessage(TextMessageDTO message, String senderUsername, String sessionId) {
        if (!sessionRoomValidator.canSessionSendMessagesToRoom(sessionId, message.getRoomId())) {
            clientErrorHandler.sendMessageErrorToSession(sessionId, "User is not allowed to send messages to the room", message.getUuid());
            return;
        }
        textMessageQueue.push(new TextMessageEvent(message,senderUsername, sessionId));
    }
}
