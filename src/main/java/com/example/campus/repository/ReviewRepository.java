package com.example.campus.repository;

import com.example.campus.entity.Order;
import com.example.campus.entity.Review;
import com.example.campus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /** 我收到的评价 */
    List<Review> findByReviewedOrderByCreatedAtDesc(User reviewed);

    /** 我发出的评价 */
    List<Review> findByReviewerOrderByCreatedAtDesc(User reviewer);

    /** 某订单是否已被某人评价 */
    boolean existsByOrderAndReviewer(Order order, User reviewer);

    /** 计算某用户收到的平均分 */
    @org.springframework.data.jpa.repository.Query(
            "SELECT AVG(r.rating) FROM Review r WHERE r.reviewed.id = :userId")
    Double averageRating(@org.springframework.data.repository.query.Param("userId") Long userId);

    long countByReviewed(User reviewed);
}