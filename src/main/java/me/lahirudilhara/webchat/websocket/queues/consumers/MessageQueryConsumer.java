package me.lahirudilhara.webchat.websocket.queues.consumers;

import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.dto.message.MessageResponseDTO;
import me.lahirudilhara.webchat.dtoEntityMappers.api.MessageMapper;
import me.lahirudilhara.webchat.entities.message.MessageEntity;
import me.lahirudilhara.webchat.service.message.RoomMessageQueryService;
import me.lahirudilhara.webchat.websocket.interfaces.ClientErrorHandler;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageBroker;
import me.lahirudilhara.webchat.websocket.queues.MessageQueryQueue;
import me.lahirudilhara.webchat.websocket.queues.events.MessageQueryEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MessageQueryConsumer {
    private final MessageQueryQueue messageQueryQueue;
    private final RoomMessageQueryService roomMessageQueryService;
    private final ClientErrorHandler clientErrorHandler;
    private final MessageBroker messageBroker;
    private final MessageMapper messageMapper;

    public MessageQueryConsumer(MessageQueryQueue messageQueryQueue, RoomMessageQueryService roomMessageQueryService, ClientErrorHandler clientErrorHandler, MessageBroker messageBroker, MessageMapper messageMapper) {
        this.messageQueryQueue = messageQueryQueue;
        this.roomMessageQueryService = roomMessageQueryService;
        this.clientErrorHandler = clientErrorHandler;
        this.messageBroker = messageBroker;
        this.messageMapper = messageMapper;
    }

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void consume(ApplicationReadyEvent readyEvent) {
        log.info("Message Query Consumer started");
        while(true) {
            try{
                MessageQueryEvent event = messageQueryQueue.poll();
                handleMessageQueryEvent(event);
            } catch (InterruptedException e) {
                log.error("There was an error while consuming the message in MessageQueryQueue.", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void handleMessageQueryEvent(MessageQueryEvent messageQueryEvent) {
        List<MessageEntity> messages = new ArrayList<>();
        String sessionId = messageQueryEvent.sessionId();
        String username = messageQueryEvent.sender();
        String uuid = messageQueryEvent.remainMessageDTO().getUuid();
        int roomId = messageQueryEvent.remainMessageDTO().getRoomId();
        try{
            messages = roomMessageQueryService.getMessagesAfterGivenMessageIdInRoom(roomId,messageQueryEvent.remainMessageDTO().getMessageId(),username);
        } catch (Exception e) {
            log.error("There was an error while processing the message in MessageQueryQueue.", e);
            clientErrorHandler.sendMessageErrorToSession(sessionId,"Unexpected error occurred while querying the messages",uuid);
            return;
        }
        if(messages.isEmpty()){
            clientErrorHandler.sendMessageErrorToSession(sessionId,"There is no new messages",uuid);
            return;
        }
        messages.forEach(message -> sendMessageEntity(message,sessionId,uuid));

    }

    private void sendMessageEntity(MessageEntity messageEntity, String sessionId, String uuid) {
        MessageResponseDTO messageResponseDTO = messageMapper.messageEntityToMessageResponseDTO(messageEntity);
        messageResponseDTO.setUuid(uuid);
        messageBroker.sendMessageToSession(sessionId, messageResponseDTO);
    }
}
