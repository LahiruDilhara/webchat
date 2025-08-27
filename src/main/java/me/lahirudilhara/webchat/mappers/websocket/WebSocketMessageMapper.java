package me.lahirudilhara.webchat.mappers.websocket;

import me.lahirudilhara.webchat.dto.websocket.user.UserTextMessageDto;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.models.message.TextMessage;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WebSocketMessageMapper {
//    @BeanMapping(ignoreByDefault = true)
//    @Mapping(target = "content",source = "message")
//    Message SendMessageDtoToMessage(SendMessageDTO sendMessageDTO);
//
//    @Mapping(target = "message",source = "message.content")
//    @Mapping(target = "senderUsername", source = "senderUsername")
//    @Mapping(target = "roomId",source = "message.room.id")
//    MessageResponseDTO MessageToMessageResponseDTO(Message message, String senderUsername);


    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "content",source = "message")
    TextMessage UserTextMessageDtoToTextMessage(UserTextMessageDto userTextMessageDto);
}
