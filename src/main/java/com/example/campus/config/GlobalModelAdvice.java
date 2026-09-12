package com.example.campus.config;

import com.example.campus.entity.User;
import com.example.campus.service.MessageService;
import com.example.campus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 全局模型增强器
 * 在每个页面的Model中自动加入：当前用户、未读消息数
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAdvice {

    private final UserService userService;
    private final MessageService messageService;

    @ModelAttribute("currentUser")
    public User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        try {
            return userService.findByUsername(auth.getName());
        } catch (RuntimeException e) {
            return null;
        }
    }

    @ModelAttribute("globalUnreadCount")
    public long globalUnreadCount() {
        User user = currentUser();
        if (user == null) {
            return 0;
        }
        return messageService.countUnread(user.getUsername());
    }
}