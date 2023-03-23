package com.xuno.message.repository;

import com.xuno.message.entity.Message;
import com.xuno.message.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    @Query("SELECT ENTITY from Message ENTITY where ENTITY.sender.id = :userId or ENTITY.recipient.id = :userId and ENTITY.id = :id")
    Optional<Message> getMessageByIdEqualsAndRecipientEquals(@Param("id") Long id,@Param("userId") Long userId);



    @Query("SELECT ENTITY from Message ENTITY where ENTITY.sender.id = :userId or ENTITY.recipient.id = :userId")
    List<Message> getCurrentUsersAllMessages(@Param("userId") Long userId);


    Optional<Message> getMessageByIdEqualsAndSender_Id(Long messageId, Long senderId);

    Optional<Message> getMessageByIdEqualsAndRecipient_IdEquals(Long messageId, Long receiverId);


    @Query("SELECT ENTITY from Message ENTITY where (ENTITY.recipient.id = :senderId OR  ENTITY.recipient.id = :receiverId) AND (ENTITY.sender.id = :senderId OR  ENTITY.sender.id = :receiverId) ")
    Message getMessagesBySenderOrRecipient(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);


}
