package me.lahirudilhara.webchat.websocket.dispatcher.handlers;

import me.lahirudilhara.webchat.websocket.dto.requests.JoinRoomMessageDTO;
import me.lahirudilhara.webchat.websocket.dispatcher.MessageHandler;
import me.lahirudilhara.webchat.websocket.dto.response.NewDeviceConnectedWithRoomResponse;
import me.lahirudilhara.webchat.websocket.dto.response.NewRoomUserResponse;
import me.lahirudilhara.webchat.websocket.interfaces.ClientErrorHandler;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageBroker;
import me.lahirudilhara.webchat.websocket.lib.interfaces.RoomBroker;
import me.lahirudilhara.webchat.websocket.services.JoinRoomValidator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JoinRoomRequestMessageHandler implements MessageHandler<JoinRoomMessageDTO> {
    private final JoinRoomValidator joinRoomValidator;
    private final RoomBroker roomBroker;
    private final ClientErrorHandler clientErrorHandler;

    public JoinRoomRequestMessageHandler(JoinRoomValidator joinRoomValidator, RoomBroker roomBroker, ClientErrorHandler clientErrorHandler) {
        this.joinRoomValidator = joinRoomValidator;
        this.roomBroker = roomBroker;
        this.clientErrorHandler = clientErrorHandler;
    }

    @Override
    public Class<JoinRoomMessageDTO> getMessageClassType() {
        return JoinRoomMessageDTO.class;
    }

    @Override
    public void handleMessage(JoinRoomMessageDTO message, String senderUsername, String sessionId) {
        var validation = joinRoomValidator.validateJoinRoom(senderUsername, message.getRoomId());
        if (validation.isLeft()) {
            clientErrorHandler.sendMessageErrorToSession(sessionId,"Invalid join room",message.getUuid());
            return;
        }
        roomBroker.addSessionToRoom(message.getRoomId(), sessionId, senderUsername);
    }
}
