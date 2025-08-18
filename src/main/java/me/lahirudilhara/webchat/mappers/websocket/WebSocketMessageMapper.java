package me.lahirudilhara.webchat.mappers.websocket;

import me.lahirudilhara.webchat.dto.websocket.message.SendMessageDTO;
import me.lahirudilhara.webchat.models.Message;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WebSocketMessageMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "content",source = "message")
    Message SendMessageDtoToMessage(SendMessageDTO sendMessageDTO);
}
