package com.xuno.message.dto.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {
    SUCCESS(101, "SUCCESS"),
    FAILED(102, "FAILED"),
    INVALID(999, "Invalid Request"),
    BLOCKED(401, "BLOCKED"),
    MESSAGE_SENT(201, "MESSAGE SENT");

    private final Integer status;
    private final String message;

    StatusEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }


}

