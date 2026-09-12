package com.example.campus.service;

import com.example.campus.entity.*;
import com.example.campus.repository.OrderRepository;
import com.example.campus.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final UserService userService;

    /**
     * 买家下单
     */
    @Transactional
    public Order createOrder(Long productId, String buyerUsername, String remark) {
        Product product = productService.findById(productId);
        User buyer = userService.findByUsername(buyerUsername);

        if (product.getSeller().getId().equals(buyer.getId())) {
            throw new RuntimeException("不能购买自己发布的商品");
        }
        if (product.getStatus() != ProductStatus.AVAILABLE) {
            throw new RuntimeException("该商品已不可购买");
        }

        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .buyer(buyer)
                .seller(product.getSeller())
                .product(product)
                .amount(product.getPrice())
                .remark(remark)
                .status(OrderStatus.PENDING)
                .build();
        orderRepository.save(order);

        // 下单后锁定商品
        product.setStatus(ProductStatus.SOLD);
        productRepository.save(product);
        return order;
    }

    public List<Order> myBoughtOrders(String username) {
        return orderRepository.findByBuyerOrderByCreatedAtDesc(userService.findByUsername(username));
    }

    public List<Order> mySoldOrders(String username) {
        return orderRepository.findBySellerOrderByCreatedAtDesc(userService.findByUsername(username));
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    /**
     * 卖家确认订单，进入交易中
     */
    @Transactional
    public void confirm(Long orderId, String sellerUsername) {
        Order order = findById(orderId);
        checkSeller(order, sellerUsername);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("当前状态无法确认");
        }
        order.setStatus(OrderStatus.TRADING);
        orderRepository.save(order);
    }

    /**
     * 买家确认完成交易
     */
    @Transactional
    public void complete(Long orderId, String buyerUsername) {
        Order order = findById(orderId);
        checkBuyer(order, buyerUsername);
        if (order.getStatus() != OrderStatus.TRADING) {
            throw new RuntimeException("当前状态无法完成");
        }
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }

    /**
     * 取消订单（买卖双方均可），商品重新上架
     */
    @Transactional
    public void cancel(Long orderId, String username) {
        Order order = findById(orderId);
        if (!order.getBuyer().getUsername().equals(username)
                && !order.getSeller().getUsername().equals(username)) {
            throw new RuntimeException("无权操作该订单");
        }
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new RuntimeException("已完成的订单无法取消");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        Product product = order.getProduct();
        if (product.getStatus() == ProductStatus.SOLD) {
            product.setStatus(ProductStatus.AVAILABLE);
            productRepository.save(product);
        }
    }

    public long countBought(String username) {
        return orderRepository.countByBuyer(userService.findByUsername(username));
    }

    public long countSold(String username) {
        return orderRepository.countBySeller(userService.findByUsername(username));
    }

    private void checkSeller(Order order, String username) {
        if (!order.getSeller().getUsername().equals(username)) {
            throw new RuntimeException("无权操作该订单");
        }
    }

    private void checkBuyer(Order order, String username) {
        if (!order.getBuyer().getUsername().equals(username)) {
            throw new RuntimeException("无权操作该订单");
        }
    }

    private String generateOrderNumber() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int rand = ThreadLocalRandom.current().nextInt(1000, 9999);
        return time + rand;
    }
}