package com.xuno.message.service;

import com.xuno.message.dto.EditMessageDTO;
import com.xuno.message.dto.MessageDto;
import com.xuno.message.dto.MessageResponseDTO;
import org.springframework.http.ResponseEntity;

public interface MessageService {

    ResponseEntity<MessageResponseDTO<?>> sendMessage(MessageDto messageDto);
    ResponseEntity<MessageResponseDTO<?>> editMessage(EditMessageDTO messageDto);
    ResponseEntity<MessageResponseDTO<?>> deleteMessage(Long userId, Long messageContentId);
    ResponseEntity<MessageResponseDTO<?>> approveMessage(Long userId, Long messageId);
    ResponseEntity<MessageResponseDTO<?>> getMessageContents(Long userId, Long messageId, int page);
    ResponseEntity<MessageResponseDTO<?>> getMessages(Long userId);

    ResponseEntity<MessageResponseDTO<?>> blockMessage(Long userId, Long messageId,Long blockUserId);
}
