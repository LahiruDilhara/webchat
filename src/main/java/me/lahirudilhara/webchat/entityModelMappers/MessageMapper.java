package me.lahirudilhara.webchat.entityModelMappers;

import me.lahirudilhara.webchat.entities.message.MessageEntity;
import me.lahirudilhara.webchat.entities.message.TextMessageEntity;
import me.lahirudilhara.webchat.models.message.Message;
import me.lahirudilhara.webchat.models.message.TextMessage;
import org.mapstruct.*;

@Mapper(componentModel = "spring",implementationName = "entityModelMessageMapper")
public interface MessageMapper {

    @SubclassMapping(source = TextMessage.class,target = TextMessageEntity.class)
    MessageEntity messageToMessageEntity(Message message);

    @Mapping(target = "senderId",source = "sender.id")
    @Mapping(target = "senderUsername",source = "sender.username")
    @Mapping(target = "roomId",source = "room.id")
    MessageEntity baseMessageToMessageEntity(Message message);

    @InheritConfiguration(name = "baseMessageToMessageEntity")
    TextMessageEntity textMessageToTextMessageEntity(TextMessage message);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,ignoreByDefault = true)
    @Mapping(source = "content",target = "content")
    @Mapping(source = "editedAt",target = "editedAt")
    void updateTextMessageEntityToTextMessage(TextMessageEntity textMessageEntity,@MappingTarget TextMessage textMessage);
}
