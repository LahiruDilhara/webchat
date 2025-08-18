package me.lahirudilhara.webchat.dto.websocket.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class SendMessageDTO {
    @NotBlank(message = "Message cannot be blank")
    private String message;

    @Min(value = 1,message = "RoomId must be a positive integer")
    private int roomId;
    private static ObjectMapper mapper = new ObjectMapper();

    public SendMessageDTO() {
    }

    public SendMessageDTO(String message, int roomId) {
        this.message = message;
        this.roomId = roomId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public static SendMessageDTO fromJson(String json) throws JsonProcessingException {
        SendMessageDTO dto = null;
        dto = mapper.readValue(json,SendMessageDTO.class);
        return dto;
    }
}
