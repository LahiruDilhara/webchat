package me.lahirudilhara.webchat.mappers.websocket;

import me.lahirudilhara.webchat.dto.message.TextMessageResponseDTO;
import me.lahirudilhara.webchat.dto.wc.TextMessageAckResponseDTO;
import me.lahirudilhara.webchat.dto.wc.TextMessageDTO;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.models.message.TextMessage;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WebSocketMessageMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "content",source = "message")
    TextMessage textMessageDtoToTextMessage(TextMessageDTO textMessageDTO);

    @Mapping(source = "textMessage.sender.username",target = "senderUsername")
    @Mapping(source = "textMessage.sender.id",target = "senderId")
    @Mapping(source = "textMessage.room.id",target = "roomId")
    @Mapping(source = "uuid",target = "uuid")
    TextMessageAckResponseDTO textMessageToTextMessageAckResponseDTO(TextMessage textMessage, String uuid);
}
