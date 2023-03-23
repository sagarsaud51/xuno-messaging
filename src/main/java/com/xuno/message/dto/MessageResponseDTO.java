package com.xuno.message.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO<T> {

    private Integer status;
    private String message;
    private Boolean success;
    private T data;

}
