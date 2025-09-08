package me.lahirudilhara.webchat.dtoEntityMappers.api;

import me.lahirudilhara.webchat.dto.message.MessageResponseDTO;
import me.lahirudilhara.webchat.dto.message.TextMessageResponseDTO;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.models.message.TextMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(source = "sender.username",target = "senderUsername")
    @Mapping(source = "sender.id",target = "senderId")
    @Mapping(source = "room.id",target = "roomId")
    @SubclassMapping(source = TextMessage.class,target = TextMessageResponseDTO.class)
    MessageResponseDTO messageToMessageResponse(Message message);

    TextMessageResponseDTO textMessageToTextMessageResponseDTO(TextMessage textMessage);
}
