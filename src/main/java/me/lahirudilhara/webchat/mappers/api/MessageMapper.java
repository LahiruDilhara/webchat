package me.lahirudilhara.webchat.mappers.api;

import me.lahirudilhara.webchat.dto.api.message.UpdateMessageDTO;
import me.lahirudilhara.webchat.dto.message.MessageResponseDTO;
import me.lahirudilhara.webchat.dto.message.TextMessageResponseDTO;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.models.message.TextMessage;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.w3c.dom.Text;

@Mapper(componentModel = "spring")
public interface MessageMapper {

//    default MessageResponseDTO toDto(Message message){
//        if(message instanceof TextMessage){
//            return textMessageToTextMessageResponseDTO((TextMessage)message);
//        }
//        throw new IllegalArgumentException("Unknown message type");
//    }
    @Mapping(source = "sender.username",target = "senderUsername")
    @Mapping(source = "sender.id",target = "senderId")
    @Mapping(source = "room.id",target = "roomId")
    MessageResponseDTO messageToMessageResponse(Message message);

    TextMessageResponseDTO textMessageToTextMessageResponseDTO(TextMessage textMessage);
}
