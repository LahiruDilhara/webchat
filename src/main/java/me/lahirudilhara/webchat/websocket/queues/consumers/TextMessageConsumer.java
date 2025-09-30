package me.lahirudilhara.webchat.websocket.queues.consumers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import me.lahirudilhara.webchat.dto.message.MessageResponseDTO;
import me.lahirudilhara.webchat.dtoEntityMappers.api.MessageMapper;
import me.lahirudilhara.webchat.entities.message.MessageEntity;
import me.lahirudilhara.webchat.entities.message.TextMessageEntity;
import me.lahirudilhara.webchat.service.message.MessageManagementService;
import me.lahirudilhara.webchat.websocket.dto.requests.TextMessageDTO;
import me.lahirudilhara.webchat.websocket.interfaces.ClientErrorHandler;
import me.lahirudilhara.webchat.websocket.lib.interfaces.MessageBroker;
import me.lahirudilhara.webchat.websocket.queues.TextMessageQueue;
import me.lahirudilhara.webchat.websocket.queues.events.TextMessageEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class TextMessageConsumer {

    private final TextMessageQueue textMessageQueue;
    private final MessageMapper messageEntityMapper;
    private final MessageBroker messageBroker;
    private final ClientErrorHandler clientErrorHandler;
    private final MessageManagementService messageManagementService;

    @PersistenceContext
    private EntityManager entityManager;


    public TextMessageConsumer(TextMessageQueue textMessageQueue, MessageMapper messageEntityMapper, MessageBroker messageBroker, ClientErrorHandler clientErrorHandler, MessageManagementService messageManagementService) {
        this.textMessageQueue = textMessageQueue;
        this.messageEntityMapper = messageEntityMapper;
        this.messageBroker = messageBroker;
        this.clientErrorHandler = clientErrorHandler;
        this.messageManagementService = messageManagementService;
    }

    @Async
    @EventListener
    public void consume(ApplicationReadyEvent applicationReadyEvent) {
        log.info("TextMessageConsumer started");
        while (true) {
            try{
                TextMessageEvent event = textMessageQueue.poll();
                handleTextMessageEvent(event);
            } catch (InterruptedException e) {
                log.error("There was an error while consuming the message in TextMessageQueue",e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void handleTextMessageEvent(TextMessageEvent textMessageEvent) {
        int roomId = textMessageEvent.message().getRoomId();
        String uuid = textMessageEvent.message().getUuid();
        try{
            TextMessageEntity textMessageEntity = textMessageEntityFromTextMessageDTO(textMessageEvent.message(),textMessageEvent.senderUsername());
            MessageEntity messageEntity = messageManagementService.addTextMessage(textMessageEntity,roomId);
            sendMessageEntityToRoom(messageEntity,roomId,uuid);
            return;
        } catch (RuntimeException e) {
            log.error("There was an error while processing the message in TextMessageQueue.",e);
        } catch (Exception e) {
            log.error("There was an error while processing the message in TextMessageQueue",e);
        }
        clientErrorHandler.sendMessageErrorToSession(textMessageEvent.sessionId(),"Error occoured while saving the message",uuid);
    }

    private TextMessageEntity textMessageEntityFromTextMessageDTO(TextMessageDTO textMessageDTO,String username) {
        TextMessageEntity textMessageEntity = new TextMessageEntity();
        textMessageEntity.setSenderUsername(username);
        textMessageEntity.setContent(textMessageDTO.getMessage());
        return textMessageEntity;
    }

    private void sendMessageEntityToRoom(MessageEntity messageEntity, int roomId, String uuid) {
        MessageResponseDTO messageResponseDTO = messageEntityMapper.messageEntityToMessageResponseDTO(messageEntity);
        messageResponseDTO.setUuid(uuid);
        messageBroker.sendMessageToRoom(roomId,messageResponseDTO);
    }

}
