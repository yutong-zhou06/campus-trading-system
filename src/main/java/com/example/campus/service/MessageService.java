package com.example.campus.service;

import com.example.campus.entity.Message;
import com.example.campus.entity.Product;
import com.example.campus.entity.User;
import com.example.campus.repository.MessageRepository;
import com.example.campus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ProductService productService;

    /**
     * 发送私信
     */
    @Transactional
    public Message send(String senderUsername, Long receiverId, Long productId, String content) {
        User sender = userService.findByUsername(senderUsername);
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("接收者不存在"));
        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException("不能给自己发消息");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new RuntimeException("消息内容不能为空");
        }
        Product product = productId == null ? null : productService.findById(productId);

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .product(product)
                .content(content.trim())
                .isRead(false)
                .build();
        return messageRepository.save(message);
    }

    /**
     * 获取与某人的聊天记录，并把对方发来的消息标记为已读
     */
    @Transactional
    public List<Message> conversation(Long myId, Long otherId) {
        messageRepository.markAsRead(otherId, myId);
        return messageRepository.findConversation(myId, otherId);
    }

    /**
     * 我的联系人列表
     */
    public List<User> contacts(String username) {
        User me = userService.findByUsername(username);
        List<Long> ids = messageRepository.findContactIds(me.getId());
        List<User> users = new ArrayList<>();
        for (Long id : ids) {
            userRepository.findById(id).ifPresent(users::add);
        }
        return users;
    }

    public long countUnread(String username) {
        return messageRepository.countByReceiverAndIsReadFalse(userService.findByUsername(username));
    }

    public User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }
}