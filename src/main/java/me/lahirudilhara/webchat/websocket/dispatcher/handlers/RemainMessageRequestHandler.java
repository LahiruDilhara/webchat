package me.lahirudilhara.webchat.websocket.dispatcher.handlers;

import me.lahirudilhara.webchat.websocket.dispatcher.MessageHandler;
import me.lahirudilhara.webchat.websocket.dto.requests.RemainMessageDTO;
import me.lahirudilhara.webchat.websocket.interfaces.ClientErrorHandler;
import me.lahirudilhara.webchat.websocket.queues.MessageQueryQueue;
import me.lahirudilhara.webchat.websocket.queues.events.MessageQueryEvent;
import me.lahirudilhara.webchat.websocket.services.SessionRoomValidator;
import org.springframework.stereotype.Component;

@Component
public class RemainMessageRequestHandler implements MessageHandler<RemainMessageDTO> {
    private final SessionRoomValidator sessionRoomValidator;
    private final ClientErrorHandler clientErrorHandler;
    private final MessageQueryQueue messageQueryQueue;

    public RemainMessageRequestHandler(SessionRoomValidator sessionRoomValidator, ClientErrorHandler clientErrorHandler, MessageQueryQueue messageQueryQueue) {
        this.sessionRoomValidator = sessionRoomValidator;
        this.clientErrorHandler = clientErrorHandler;
        this.messageQueryQueue = messageQueryQueue;
    }

    @Override
    public Class<RemainMessageDTO> getMessageClassType() {
        return RemainMessageDTO.class;
    }

    @Override
    public void handleMessage(RemainMessageDTO message, String senderUsername, String sessionId) {
        if (!sessionRoomValidator.canSessionSendMessagesToRoom(sessionId, message.getRoomId())) {
            clientErrorHandler.sendMessageErrorToSession(sessionId, "User is not allowed to query the room messages", message.getUuid());
            return;
        }
        messageQueryQueue.push(new MessageQueryEvent(message, senderUsername, sessionId));
    }
}
