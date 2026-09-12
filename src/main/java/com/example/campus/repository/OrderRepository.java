package com.example.campus.repository;

import com.example.campus.entity.Order;
import com.example.campus.entity.OrderStatus;
import com.example.campus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /** 我买到的订单 */
    List<Order> findByBuyerOrderByCreatedAtDesc(User buyer);

    /** 我卖出的订单 */
    List<Order> findBySellerOrderByCreatedAtDesc(User seller);

    /** 某商品是否已有进行中的订单 */
    boolean existsByProductIdAndStatusIn(Long productId, List<OrderStatus> statuses);

    long countByBuyer(User buyer);

    long countBySeller(User seller);
}