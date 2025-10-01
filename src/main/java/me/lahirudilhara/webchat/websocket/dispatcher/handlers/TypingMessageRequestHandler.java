package me.lahirudilhara.webchat.websocket.dispatcher.handlers;

import me.lahirudilhara.webchat.websocket.dispatcher.MessageHandler;
import me.lahirudilhara.webchat.websocket.dto.requests.TypingMessageDTO;
import me.lahirudilhara.webchat.websocket.dto.response.TypingResponse;
import me.lahirudilhara.webchat.websocket.interfaces.ClientErrorHandler;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageBroker;
import me.lahirudilhara.webchat.websocket.services.SessionRoomValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypingMessageRequestHandler implements MessageHandler<TypingMessageDTO> {
    private final SessionRoomValidator sessionRoomValidator;
    private final ClientErrorHandler clientErrorHandler;
    private final MessageBroker messageBroker;

    public TypingMessageRequestHandler(SessionRoomValidator sessionRoomValidator, ClientErrorHandler clientErrorHandler, MessageBroker messageBroker) {
        this.sessionRoomValidator = sessionRoomValidator;
        this.clientErrorHandler = clientErrorHandler;
        this.messageBroker = messageBroker;
    }

    @Override
    public Class<TypingMessageDTO> getMessageClassType() {
        return  TypingMessageDTO.class;
    }

    @Override
    public void handleMessage(TypingMessageDTO message, String senderUsername, String sessionId) {
        if (!sessionRoomValidator.canSessionSendMessagesToRoom(sessionId, message.getRoomId())) {
            clientErrorHandler.sendMessageErrorToSession(sessionId, "User is not allowed to send typing to room", message.getUuid());
            return;
        }
        messageBroker.sendMessageToRoomExceptUsers(message.getRoomId(), List.of(senderUsername), TypingResponse.builder().roomId(message.getRoomId()).username(senderUsername).uuid(message.getUuid()).build());
    }
}
