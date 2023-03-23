package com.xuno.message.repository;

import com.xuno.message.entity.MessageContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MessageContentRepository extends JpaRepository<MessageContent, Long>, PagingAndSortingRepository<MessageContent, Long> {

    Page<MessageContent> findMessageContentsByMessage_IdAndDeletedEquals(Long message_id, Pageable pageable, Boolean deleted);

    MessageContent findMessageContentsByIdEqualsAndSenderIdAndReceiverId(Long messageId, Long senderId, Long receiverId);
    Optional<MessageContent> findMessageContentsByIdEqualsAndSenderId(Long messageId, Long senderId);


}
