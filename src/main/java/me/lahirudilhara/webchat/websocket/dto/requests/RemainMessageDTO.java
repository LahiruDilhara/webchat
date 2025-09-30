package me.lahirudilhara.webchat.websocket.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemainMessageDTO extends BaseRequestMessageDTO {

    @NotNull(message = "The messageId cannot be null")
    @Min(value = 1,message = "The messageId should be greater than 0")
    private Integer messageId;
}
