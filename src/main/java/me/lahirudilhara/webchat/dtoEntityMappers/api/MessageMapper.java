package me.lahirudilhara.webchat.dtoEntityMappers.api;

import me.lahirudilhara.webchat.dto.api.message.UpdateMessageDTO;
import me.lahirudilhara.webchat.dto.message.MessageResponseDTO;
import me.lahirudilhara.webchat.dto.message.TextMessageResponseDTO;
import me.lahirudilhara.webchat.entities.message.MessageEntity;
import me.lahirudilhara.webchat.entities.message.TextMessageEntity;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.models.message.TextMessage;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @SubclassMapping(source = TextMessageEntity.class,target = TextMessageResponseDTO.class)
    MessageResponseDTO messageEntityToMessageResponseDTO(MessageEntity message);

    MessageResponseDTO baseMessageEntityToMessageResponseDTO(MessageEntity message);

    TextMessageResponseDTO textMessageEntityToTextMessageResponseDTO(TextMessageEntity message);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,ignoreByDefault = true)
    @Mapping(target = "content",source = "updateMessageDTO.message")
    @Mapping(target = "roomId",source = "updateMessageDTO.roomId")
    @Mapping(target = "senderUsername", source = "username")
    TextMessageEntity updateMessageDTOToMessageEntity(UpdateMessageDTO updateMessageDTO, String username);
}
