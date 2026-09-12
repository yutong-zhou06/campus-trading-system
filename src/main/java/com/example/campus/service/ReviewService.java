package com.example.campus.service;

import com.example.campus.entity.Order;
import com.example.campus.entity.Review;
import com.example.campus.entity.User;
import com.example.campus.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final OrderService orderService;

    /**
     * 提交评价
     */
    @Transactional
    public Review create(Long orderId, String reviewerUsername, Integer rating, String content) {
        Order order = orderService.findById(orderId);
        User reviewer = userService.findByUsername(reviewerUsername);

        boolean isBuyer = order.getBuyer().getId().equals(reviewer.getId());
        boolean isSeller = order.getSeller().getId().equals(reviewer.getId());
        if (!isBuyer && !isSeller) {
            throw new RuntimeException("无权评价该订单");
        }
        if (order.getStatus() != com.example.campus.entity.OrderStatus.COMPLETED) {
            throw new RuntimeException("订单完成后才能评价");
        }
        if (reviewRepository.existsByOrderAndReviewer(order, reviewer)) {
            throw new RuntimeException("你已经评价过该订单");
        }
        if (rating == null || rating < 1 || rating > 5) {
            throw new RuntimeException("评分必须在1-5之间");
        }

        User reviewed = isBuyer ? order.getSeller() : order.getBuyer();

        Review review = Review.builder()
                .order(order)
                .reviewer(reviewer)
                .reviewed(reviewed)
                .rating(rating)
                .content(content)
                .build();
        reviewRepository.save(review);

        if (isBuyer) {
            order.setBuyerReviewed(true);
        } else {
            order.setSellerReviewed(true);
        }
        return review;
    }

    public List<Review> receivedReviews(String username) {
        return reviewRepository.findByReviewedOrderByCreatedAtDesc(userService.findByUsername(username));
    }

    public double averageRating(String username) {
        User user = userService.findByUsername(username);
        Double avg = reviewRepository.averageRating(user.getId());
        return avg == null ? 0.0 : Math.round(avg * 10) / 10.0;
    }

    public long countReviews(String username) {
        return reviewRepository.countByReviewed(userService.findByUsername(username));
    }
}