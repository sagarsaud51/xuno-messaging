package com.xuno.message.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class EditMessageDTO extends MessageDto {

    @NotNull(message = "Sender ID is required.")
    Long messageId;

}
