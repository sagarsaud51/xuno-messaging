package com.xuno.message.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xuno.message.config.MessageEncrypt;
import com.xuno.message.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "messages_content")
@Data
@EqualsAndHashCode(callSuper = true)
public class MessageContent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    @JsonIgnore
    private Message message;

    @Column(nullable = false)
    @Convert(converter = MessageEncrypt.class)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;


}
