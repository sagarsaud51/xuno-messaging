package com.xuno.message.util;

import com.xuno.message.dto.MessageResponseDTO;
import com.xuno.message.dto.enums.StatusEnum;

import java.util.Objects;

public class ResponseUtil {

    public static MessageResponseDTO<?> responseGenerator(Object data, StatusEnum statusEnum, Boolean success) {
        MessageResponseDTO<Object> messageResponseDTO = new MessageResponseDTO<Object>();
        messageResponseDTO.setSuccess(success);
        messageResponseDTO.setMessage(statusEnum.getMessage());
        messageResponseDTO.setStatus(statusEnum.getStatus());
        if (!Objects.isNull(data)) {
            messageResponseDTO.setData(data);
        }
        return messageResponseDTO;
    }
}
