package com.xuno.message.service.impl;


import com.xuno.message.dto.EditMessageDTO;
import com.xuno.message.dto.MessageDto;
import com.xuno.message.dto.MessageResponseDTO;
import com.xuno.message.dto.enums.StatusEnum;
import com.xuno.message.entity.Message;
import com.xuno.message.entity.MessageContent;
import com.xuno.message.entity.MessageStatus;
import com.xuno.message.entity.User;
import com.xuno.message.exception.XunoException;
import com.xuno.message.repository.MessageContentRepository;
import com.xuno.message.repository.MessageRepository;
import com.xuno.message.repository.UserRepository;
import com.xuno.message.service.MessageService;
import com.xuno.message.util.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {


    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageContentRepository messageContentRepository;

    private final String SENDER = "SENDER";
    private final String RECEIVER = "RECEIVER";

    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository, MessageContentRepository messageContentRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageContentRepository = messageContentRepository;
    }

    @Override
    public ResponseEntity<MessageResponseDTO<?>> sendMessage(MessageDto messageDto) {
        try {
            Map<String, User> userMap = validateUser(messageDto);
            Message message = messageRepository
                    .getMessagesBySenderOrRecipient(userMap.get(SENDER).getId(), userMap.get(RECEIVER).getId());
            if (Objects.isNull(message)) {
                message = messageRepository.save(messageDTOToEntity(messageDto, userMap));
            }
            if (message.getBlocked()) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(ResponseUtil.responseGenerator(StatusEnum.BLOCKED.getMessage(), StatusEnum.BLOCKED, false));
            }
            if (!message.getApproved() && Objects.equals(message.getRecipient().getId(), messageDto.getSender())) {
                this.approveMessage(message.getRecipient().getId(), message.getId());
            }
            MessageContent messageContent = messageContentRepository.save(saveMessageContent(messageDto, message, userMap));
            return ResponseEntity.ok(ResponseUtil.responseGenerator(messageContent, StatusEnum.MESSAGE_SENT, true));

        } catch (Exception e) {
            System.out.println(e instanceof XunoException);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtil.responseGenerator(e.getMessage(), StatusEnum.FAILED, false));
        }
    }


    @Override
    public ResponseEntity<MessageResponseDTO<?>> editMessage(EditMessageDTO messageDto) {
        try {
            MessageContent message = messageContentRepository.findMessageContentsByIdEqualsAndSenderIdAndReceiverId(messageDto.getMessageId(), messageDto.getSender(), messageDto.getRecipient());

            if (Objects.isNull(message)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtil.responseGenerator(StatusEnum.INVALID.getMessage(), StatusEnum.INVALID, false));
            }

            message.setContent(messageDto.getMessage());
            message.setStatus(MessageStatus.EDITED);
            messageContentRepository.save(message);
            return ResponseEntity.ok(ResponseUtil.responseGenerator(null, StatusEnum.SUCCESS, true));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.responseGenerator(e.getMessage(), StatusEnum.FAILED, false));
        }
    }

    @Override
    public ResponseEntity<MessageResponseDTO<?>> deleteMessage(Long userId, Long messageContentId) {
        try {
            Optional<MessageContent> messageContent = messageContentRepository.findMessageContentsByIdEqualsAndSenderId(messageContentId, userId);
            if (messageContent.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtil.responseGenerator(StatusEnum.INVALID.getMessage(), StatusEnum.INVALID, false));
            }
            MessageContent msgContent = messageContent.get();
            msgContent.setDeleted(true);
            msgContent.setStatus(MessageStatus.DELETED);
            messageContentRepository.save(msgContent);
            return ResponseEntity.ok(ResponseUtil.responseGenerator(null, StatusEnum.SUCCESS, true));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.responseGenerator(e.getMessage(), StatusEnum.FAILED, false));
        }
    }

    @Override
    public ResponseEntity<MessageResponseDTO<?>> approveMessage(Long userId, Long messageId) {
        try {
            Optional<Message> message = messageRepository.getMessageByIdEqualsAndRecipient_IdEquals(userId, messageId);
            if (message.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtil.responseGenerator(StatusEnum.INVALID.getMessage(), StatusEnum.INVALID, false));
            }
            Message msg = message.get();
            msg.setApproved(true);
            messageRepository.save(msg);
            return ResponseEntity.ok(ResponseUtil.responseGenerator(null, StatusEnum.SUCCESS, true));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.responseGenerator(e.getMessage(), StatusEnum.FAILED, false));
        }
    }

    @Override
    public ResponseEntity<MessageResponseDTO<?>> getMessageContents(Long userId, Long messageId, int page) {
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                throw new XunoException("Bad Request!!");
            }

            Optional<Message> message = messageRepository.getMessageByIdEqualsAndRecipientEquals(messageId, user.get().getId());
            if (message.isEmpty()) {
                throw new XunoException("Bad Request!!");
            }
            Page<MessageContent> messageContents = messageContentRepository
                    .findMessageContentsByMessage_IdAndDeletedEquals(message.get().getId(), PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id")), false);

            messageContents.get().forEach(messageContent -> {
                if (!messageContent.getStatus().equals(MessageStatus.EDITED)) {
                    messageContent.setStatus(MessageStatus.READ);
                    messageContentRepository.save(messageContent);
                }

            });
            return ResponseEntity.ok(ResponseUtil.responseGenerator(messageContents, StatusEnum.SUCCESS, true));

        } catch (XunoException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.responseGenerator(e.getMessage(), StatusEnum.FAILED, false));
        }
    }

    @Override
    public ResponseEntity<MessageResponseDTO<?>> getMessages(Long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                throw new XunoException("Bad Request!!");
            }
            var messages = messageRepository.getCurrentUsersAllMessages(userId);
            return ResponseEntity.ok(ResponseUtil.responseGenerator(messages, StatusEnum.SUCCESS, true));
        } catch (XunoException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.responseGenerator(e.getMessage(), StatusEnum.FAILED, false));
        }
    }

    @Override
    public ResponseEntity<MessageResponseDTO<?>> blockMessage(Long userId, Long messageId, Long blockUserId) {
        try {
            Optional<Message> message = messageRepository.findById(messageId);
            if (message.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtil.responseGenerator(StatusEnum.INVALID.getMessage(), StatusEnum.INVALID, false));
            }
            Message msg = message.get();
            System.out.println(msg.getSender().getId());
            System.out.println(msg.getRecipient().getId());
            System.out.println(Objects.equals(msg.getSender().getId(), userId));
            System.out.println(Objects.equals(msg.getSender().getId(), blockUserId));
            System.out.println(Objects.equals(msg.getRecipient().getId(), blockUserId));
            System.out.println(Objects.equals(msg.getRecipient().getId(), userId));
            if (
                    !(Objects.equals(msg.getSender().getId(), userId) || Objects.equals(msg.getSender().getId(), blockUserId))
                            &&
                            !(Objects.equals(msg.getRecipient().getId(), userId) || Objects.equals(msg.getRecipient().getId(), blockUserId))
            ) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtil.responseGenerator(StatusEnum.INVALID.getMessage(), StatusEnum.INVALID, false));
            }
            msg.setBlocked(true);
            messageRepository.save(msg);
            return ResponseEntity.ok(ResponseUtil.responseGenerator(null, StatusEnum.BLOCKED, true));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.responseGenerator(e.getMessage(), StatusEnum.FAILED, false));
        }
    }


    private Map<String, User> validateUser(MessageDto dto) throws XunoException {
        User sender = userRepository.getById(dto.getSender());
        User receiver = userRepository.getById(dto.getRecipient());
        if (Objects.isNull(receiver.getId()) || Objects.isNull(sender.getId())) {
            throw new XunoException("Invalid Request");
        }
        Map<String, User> userMap = new HashMap<>();
        userMap.put(SENDER, sender);
        userMap.put(RECEIVER, receiver);
        return userMap;
    }

    private MessageContent saveMessageContent(MessageDto dto, Message message, Map<String, User> userMap) {
        MessageContent messageContent = new MessageContent();
        messageContent.setContent(dto.getMessage());
        messageContent.setMessage(message);
        messageContent.setStatus(MessageStatus.SENT);
        messageContent.setSender(userMap.get(SENDER));
        messageContent.setReceiver(userMap.get(RECEIVER));
        return messageContent;
    }

    private Message messageDTOToEntity(MessageDto messageDto, Map<String, User> userMap) {
        Message message = new Message();
        message.setRecipient(userMap.get(RECEIVER));
        message.setSender(userMap.get(SENDER));
        return message;
    }
}
