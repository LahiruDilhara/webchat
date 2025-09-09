package me.lahirudilhara.webchat.dtoEntityMappers.websocket;

import me.lahirudilhara.webchat.dto.wc.TextMessageDTO;
import me.lahirudilhara.webchat.models.message.TextMessage;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WebSocketMessageMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "content",source = "message")
    TextMessage textMessageDtoToTextMessage(TextMessageDTO textMessageDTO);
}
