package me.lahirudilhara.webchat.websocket.dispatcher.handlers;

import me.lahirudilhara.webchat.websocket.dto.JoinRoomMessageDTO;
import me.lahirudilhara.webchat.websocket.dispatcher.MessageHandler;
import me.lahirudilhara.webchat.websocket.dto.response.NewDeviceConnectedWithRoomResponse;
import me.lahirudilhara.webchat.websocket.dto.response.NewRoomUserResponse;
import me.lahirudilhara.webchat.websocket.interfaces.ClientErrorHandler;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageBroker;
import me.lahirudilhara.webchat.websocket.lib.interfaces.RoomBroker;
import me.lahirudilhara.webchat.websocket.lib.interfaces.SessionErrorHandler;
import me.lahirudilhara.webchat.websocket.services.JoinRoomValidator;
import org.springframework.stereotype.Component;

@Component
public class JoinRoomRequestMessageHandler implements MessageHandler<JoinRoomMessageDTO> {
    private final JoinRoomValidator joinRoomValidator;
    private final SessionErrorHandler sessionErrorHandler;
    private final RoomBroker roomBroker;
    private final MessageBroker messageBroker;
    private final ClientErrorHandler clientErrorHandler;

    public JoinRoomRequestMessageHandler(JoinRoomValidator joinRoomValidator, SessionErrorHandler sessionErrorHandler, RoomBroker roomBroker, MessageBroker messageBroker, ClientErrorHandler clientErrorHandler) {
        this.joinRoomValidator = joinRoomValidator;
        this.sessionErrorHandler = sessionErrorHandler;
        this.roomBroker = roomBroker;
        this.messageBroker = messageBroker;
        this.clientErrorHandler = clientErrorHandler;
    }

    @Override
    public Class<JoinRoomMessageDTO> getMessageClassType() {
        return JoinRoomMessageDTO.class;
    }

    @Override
    public void handleMessage(JoinRoomMessageDTO message, String senderUsername, String sessionId) {
        System.out.println("Called the handleMessage");
        System.out.println(sessionId);
        var validation = joinRoomValidator.validateJoinRoom(senderUsername, message.getRoomId());
        if (validation.isLeft()) {
            clientErrorHandler.sendMessageErrorToSession(sessionId,"Invalid join room",message.getUuid());
        }
        if (!roomBroker.isUserInRoom(message.getRoomId(), senderUsername)) {
            handleNewUserRoomJoin(message.getRoomId(), senderUsername, message.getUuid());
        }
        roomBroker.addSessionToRoom(message.getRoomId(), sessionId, senderUsername);
        messageBroker.sendMessageToUser(senderUsername, NewDeviceConnectedWithRoomResponse.builder().uuid(message.getUuid()).build());
    }

    private void handleNewUserRoomJoin(int roomId,String username, String uuid){
        messageBroker.sendMessageToRoom(roomId, NewRoomUserResponse.builder().uuid(uuid).roomId(roomId).username(username).build());
    }
}
