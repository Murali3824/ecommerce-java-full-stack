package com.ecommerce.controller;

import com.ecommerce.entity.Review;
import com.ecommerce.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewRepository;

    @GetMapping
    public ResponseEntity<List<Review>> getReviews() {
        return ResponseEntity.ok(reviewRepository.getAllReviews());
    }

    @PostMapping
    public ResponseEntity<Review> postReview(@RequestBody Review review) {
        return ResponseEntity.ok(reviewRepository.save(review));
    }
}
