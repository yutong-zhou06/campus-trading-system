package com.example.campus.controller;

import com.example.campus.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /** 买家下单 */
    @PostMapping
    public String create(@RequestParam Long productId,
                         @RequestParam(required = false) String remark,
                         @AuthenticationPrincipal UserDetails user,
                         RedirectAttributes ra) {
        try {
            orderService.createOrder(productId, user.getUsername(), remark);
            ra.addFlashAttribute("success", "下单成功，等待卖家确认");
            return "redirect:/orders/bought";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/products/" + productId;
        }
    }

    /** 我买到的 */
    @GetMapping("/bought")
    public String bought(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("orders", orderService.myBoughtOrders(user.getUsername()));
        model.addAttribute("role", "buyer");
        return "orders";
    }

    /** 我卖出的 */
    @GetMapping("/sold")
    public String sold(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("orders", orderService.mySoldOrders(user.getUsername()));
        model.addAttribute("role", "seller");
        return "orders";
    }

    /** 卖家确认 */
    @PostMapping("/{id}/confirm")
    public String confirm(@PathVariable Long id,
                          @AuthenticationPrincipal UserDetails user,
                          RedirectAttributes ra) {
        try {
            orderService.confirm(id, user.getUsername());
            ra.addFlashAttribute("success", "已确认订单");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders/sold";
    }

    /** 买家完成 */
    @PostMapping("/{id}/complete")
    public String complete(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails user,
                           RedirectAttributes ra) {
        try {
            orderService.complete(id, user.getUsername());
            ra.addFlashAttribute("success", "交易已完成，快去评价吧");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders/bought";
    }

    /** 取消订单 */
    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails user,
                         @RequestParam(defaultValue = "bought") String from,
                         RedirectAttributes ra) {
        try {
            orderService.cancel(id, user.getUsername());
            ra.addFlashAttribute("success", "订单已取消");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "seller".equals(from) ? "redirect:/orders/sold" : "redirect:/orders/bought";
    }
}