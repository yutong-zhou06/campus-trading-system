package com.example.campus.controller;

import com.example.campus.entity.User;
import com.example.campus.service.ReviewService;
import com.example.campus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    /** 提交评价 */
    @PostMapping("/reviews")
    public String create(@RequestParam Long orderId,
                         @RequestParam Integer rating,
                         @RequestParam(required = false) String content,
                         @RequestParam(defaultValue = "bought") String from,
                         @AuthenticationPrincipal UserDetails user,
                         RedirectAttributes ra) {
        try {
            reviewService.create(orderId, user.getUsername(), rating, content);
            ra.addFlashAttribute("success", "评价提交成功");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "seller".equals(from) ? "redirect:/orders/sold" : "redirect:/orders/bought";
    }

    /** 查看某用户的主页（含评价） */
    @GetMapping("/users/{id}")
    public String userProfile(@PathVariable Long id, Model model) {
        User target = userService.findById(id);
        model.addAttribute("target", target);
        model.addAttribute("reviews", reviewService.receivedReviews(target.getUsername()));
        model.addAttribute("avgRating", reviewService.averageRating(target.getUsername()));
        model.addAttribute("reviewCount", reviewService.countReviews(target.getUsername()));
        return "user-profile";
    }
}