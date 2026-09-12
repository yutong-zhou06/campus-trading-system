package com.example.campus.repository;

import com.example.campus.entity.Message;
import com.example.campus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * 查询两个用户之间的所有消息（双向）
     */
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
           "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " +
           "ORDER BY m.createdAt ASC")
    List<Message> findConversation(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    /**
     * 查询与我有过对话的所有联系人ID（去重）
     */
    @Query("SELECT DISTINCT CASE WHEN m.sender.id = :userId THEN m.receiver.id ELSE m.sender.id END " +
           "FROM Message m WHERE m.sender.id = :userId OR m.receiver.id = :userId")
    List<Long> findContactIds(@Param("userId") Long userId);

    /** 未读消息数量 */
    long countByReceiverAndIsReadFalse(User receiver);

    /** 某个联系人发来的未读消息数 */
    long countBySenderAndReceiverAndIsReadFalse(User sender, User receiver);

    /** 标记某个联系人发来的消息为已读 */
    @Query("UPDATE Message m SET m.isRead = true " +
           "WHERE m.sender.id = :senderId AND m.receiver.id = :receiverId AND m.isRead = false")
    @org.springframework.data.jpa.repository.Modifying
    void markAsRead(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
}