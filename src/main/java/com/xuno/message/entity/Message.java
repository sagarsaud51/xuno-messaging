package com.xuno.message.entity;

import com.xuno.message.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;



@Entity
@Table(name = "messages")
@Data
@EqualsAndHashCode(callSuper = true)
public class Message extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Column(nullable = false)
    private Boolean approved = false;
    @Column(nullable = false)
    private Boolean blocked = false;
}


