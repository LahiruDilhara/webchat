package me.lahirudilhara.webchat.websocket.dispatcher.handlers;

import me.lahirudilhara.webchat.websocket.dispatcher.MessageHandler;
import me.lahirudilhara.webchat.websocket.dto.requests.LeaveRoomMessageDTO;
import me.lahirudilhara.webchat.websocket.dto.response.DeviceDisconnectedResponse;
import me.lahirudilhara.webchat.websocket.dto.response.RoomUserLeftResponse;
import me.lahirudilhara.webchat.websocket.interfaces.ClientErrorHandler;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageBroker;
import me.lahirudilhara.webchat.websocket.lib.interfaces.RoomBroker;
import me.lahirudilhara.webchat.websocket.services.SessionRoomValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveRoomRequestMessageHandler implements MessageHandler<LeaveRoomMessageDTO> {
    private final RoomBroker roomBroker;
    private final MessageBroker messageBroker;
    private final SessionRoomValidator sessionRoomValidator;
    private final ClientErrorHandler clientErrorHandler;

    public LeaveRoomRequestMessageHandler(RoomBroker roomBroker, MessageBroker messageBroker, SessionRoomValidator sessionRoomValidator, ClientErrorHandler clientErrorHandler) {
        this.roomBroker = roomBroker;
        this.messageBroker = messageBroker;
        this.sessionRoomValidator = sessionRoomValidator;
        this.clientErrorHandler = clientErrorHandler;
    }

    @Override
    public Class<LeaveRoomMessageDTO> getMessageClassType() {
        return  LeaveRoomMessageDTO.class;
    }

    @Override
    public void handleMessage(LeaveRoomMessageDTO message, String senderUsername, String sessionId) {
        if (!sessionRoomValidator.canSessionSendMessagesToRoom(sessionId, message.getRoomId())) {
            clientErrorHandler.sendMessageErrorToSession(sessionId, "User is not joined to the room", message.getUuid());
            return;
        }
        roomBroker.removeSessionFromRoom(message.getRoomId(), sessionId);
        if(roomBroker.isUserInRoom(message.getRoomId(), senderUsername)) {
            return;
        }
        messageBroker.sendMessageToRoom(message.getRoomId(), RoomUserLeftResponse.builder().uuid(message.getUuid()).username(senderUsername).build());
    }
}
