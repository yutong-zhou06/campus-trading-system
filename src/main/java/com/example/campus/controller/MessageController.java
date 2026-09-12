package com.example.campus.controller;

import com.example.campus.entity.Message;
import com.example.campus.entity.User;
import com.example.campus.service.MessageService;
import com.example.campus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    /** 联系人列表 */
    @GetMapping
    public String list(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("contacts", messageService.contacts(user.getUsername()));
        return "messages";
    }

    /** 与某人的聊天窗口 */
    @GetMapping("/{userId}")
    public String conversation(@PathVariable Long userId,
                               @RequestParam(required = false) Long productId,
                               @AuthenticationPrincipal UserDetails user,
                               Model model) {
        User me = userService.findByUsername(user.getUsername());
        User other = messageService.findUser(userId);
        model.addAttribute("me", me);
        model.addAttribute("other", other);
        model.addAttribute("messages", messageService.conversation(me.getId(), userId));
        model.addAttribute("productId", productId);
        return "chat";
    }

    /** 发送消息 */
    @PostMapping
    public String send(@RequestParam Long receiverId,
                       @RequestParam(required = false) Long productId,
                       @RequestParam String content,
                       @AuthenticationPrincipal UserDetails user,
                       RedirectAttributes ra) {
        try {
            messageService.send(user.getUsername(), receiverId, productId, content);
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        String url = "redirect:/messages/" + receiverId;
        if (productId != null) {
            url += "?productId=" + productId;
        }
        return url;
    }
}