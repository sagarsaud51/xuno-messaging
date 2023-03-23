package com.xuno.message.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/*
* todo add custom @ControllerAdvice for response
*
* */
@Data
public class MessageDto {

    @NotNull(message = "Recipient ID is required.")
    private Long recipient;
    @NotNull(message = "Sender ID is required.")
    private Long sender;

    @NotNull(message = "Message is required.")
    @NotBlank(message = "Message is required.")
    private String message;

}
