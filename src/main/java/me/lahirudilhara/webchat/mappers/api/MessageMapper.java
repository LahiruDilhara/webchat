package me.lahirudilhara.webchat.mappers.api;

import me.lahirudilhara.webchat.dto.api.message.UpdateMessageDTO;
import me.lahirudilhara.webchat.dto.websocket.message.MessageResponseDTO;
import me.lahirudilhara.webchat.models.Message;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(target = "message",source = "content")
    @Mapping(target = "senderUsername", source = "sentBy.username")
    @Mapping(target = "roomId",source = "room.id")
    MessageResponseDTO MessageToMessageResponseDTO(Message message);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "message",target = "content")
    Message updateMessageDTOToMessage(UpdateMessageDTO updateMessageDTO, @MappingTarget Message message);
}
