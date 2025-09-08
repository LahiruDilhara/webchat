package me.lahirudilhara.webchat.common.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CachableObject<T> {
    private T data;
}
