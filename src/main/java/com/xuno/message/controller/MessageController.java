package com.xuno.message.controller;


import com.xuno.message.dto.EditMessageDTO;
import com.xuno.message.dto.MessageDto;
import com.xuno.message.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @GetMapping(value = "{userId}/{messageId}")
    public ResponseEntity<?> getMessageContents(@PathVariable Long userId, @PathVariable Long messageId, @RequestParam(value = "page", defaultValue = "0", required = false) int page) {
        return this.messageService.getMessageContents(userId, messageId, page);
    }

    @GetMapping("{userId}")
    public ResponseEntity<?> getMessages(@PathVariable Long userId) {
        return this.messageService.getMessages(userId);
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(@Valid @RequestBody(required = true) MessageDto messageDto) {
        return messageService.sendMessage(messageDto);
    }

    @PutMapping
    public ResponseEntity<?> editMessage(@Valid @RequestBody(required = true) EditMessageDTO editMessageDTO) {
        return messageService.editMessage(editMessageDTO);
    }

    @DeleteMapping("{userId}/{messageContentId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long messageContentId, @PathVariable Long userId) {
        return messageService.deleteMessage(userId, messageContentId);
    }

    @PostMapping("{userId}/{messageId}/approve")
    public ResponseEntity<?> approveMessage(@PathVariable Long userId, @PathVariable Long messageId) {
        return messageService.approveMessage(userId, messageId);
    }

    @PostMapping("{userId}/{messageId}/block/{blockUserId}")
    public ResponseEntity<?> blockUser(@PathVariable Long userId, @PathVariable Long messageId, @PathVariable Long blockUserId) {
        return messageService.blockMessage(userId, messageId, blockUserId);
    }
}
