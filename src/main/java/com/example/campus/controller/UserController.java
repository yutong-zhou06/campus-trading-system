package com.example.campus.controller;

import com.example.campus.dto.RegisterRequest;
import com.example.campus.entity.User;
import com.example.campus.service.CategoryService;
import com.example.campus.service.MessageService;
import com.example.campus.service.OrderService;
import com.example.campus.service.ProductService;
import com.example.campus.service.ReviewService;
import com.example.campus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 用户控制器
 * 处理所有与用户相关的HTTP请求
 *
 * @Controller：标识这是一个Spring MVC控制器
 * @RequiredArgsConstructor：自动注入依赖
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProductService productService;
    private final MessageService messageService;
    private final OrderService orderService;
    private final ReviewService reviewService;
    private final CategoryService categoryService;

    /**
     * 显示首页
     * GET /
     */
    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("latestProducts", productService.latestProducts());
        model.addAttribute("productCount", productService.countAvailable());
        model.addAttribute("categories", categoryService.findAll());
        if (userDetails != null) {
            model.addAttribute("unreadCount", messageService.countUnread(userDetails.getUsername()));
        }
        return "index";
    }

    /**
     * 显示注册页面
     * GET /register
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    /**
     * 处理注册请求
     * POST /register
     */
    @PostMapping("/register")
    public String processRegister(RegisterRequest request, 
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        // 验证两次密码是否一致
        if (!request.isPasswordMatch()) {
            model.addAttribute("error", "两次输入的密码不一致");
            model.addAttribute("registerRequest", request);
            return "register";
        }

        // 验证用户名长度
        if (request.getUsername() == null || request.getUsername().length() < 3) {
            model.addAttribute("error", "用户名至少3个字符");
            model.addAttribute("registerRequest", request);
            return "register";
        }

        // 验证密码长度
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            model.addAttribute("error", "密码至少6个字符");
            model.addAttribute("registerRequest", request);
            return "register";
        }

        try {
            userService.register(request);
            redirectAttributes.addFlashAttribute("success", "注册成功，请登录");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerRequest", request);
            return "register";
        }
    }

    /**
     * 显示登录页面
     * GET /login
     */
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                 @RequestParam(value = "logout", required = false) String logout,
                                 Model model) {
        if (error != null) {
            model.addAttribute("error", "用户名或密码错误");
        }
        if (logout != null) {
            model.addAttribute("success", "已成功退出登录");
        }
        return "login";
    }

    /**
     * 显示个人中心页面
     * GET /profile
     */
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // 获取当前登录用户的信息
        User user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("boughtCount", orderService.countBought(user.getUsername()));
        model.addAttribute("soldCount", orderService.countSold(user.getUsername()));
        model.addAttribute("avgRating", reviewService.averageRating(user.getUsername()));
        model.addAttribute("reviewCount", reviewService.countReviews(user.getUsername()));
        model.addAttribute("unreadCount", messageService.countUnread(user.getUsername()));
        return "profile";
    }

    /**
     * 处理个人信息更新
     * POST /profile/update
     */
    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam String email,
                                 @RequestParam String phone,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.updateProfile(userDetails.getUsername(), email, phone);
            redirectAttributes.addFlashAttribute("success", "个人信息更新成功");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    /**
     * 显示修改密码页面
     * GET /profile/password
     */
    @GetMapping("/profile/password")
    public String showPasswordForm() {
        return "change-password";
    }

    /**
     * 处理密码修改
     * POST /profile/password
     */
    @PostMapping("/profile/password")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam String currentPassword,
                                  @RequestParam String newPassword,
                                  @RequestParam String confirmPassword,
                                  RedirectAttributes redirectAttributes) {
        // 验证新密码和确认密码是否一致
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "两次输入的新密码不一致");
            return "redirect:/profile/password";
        }

        // 验证新密码长度
        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "新密码至少6个字符");
            return "redirect:/profile/password";
        }

        try {
            userService.changePassword(userDetails.getUsername(), currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("success", "密码修改成功");
            return "redirect:/profile";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/profile/password";
        }
    }
}