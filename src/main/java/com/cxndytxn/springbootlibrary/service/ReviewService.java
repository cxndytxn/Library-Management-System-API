package com.cxndytxn.springbootlibrary.service;

import com.cxndytxn.springbootlibrary.entity.Review;
import com.cxndytxn.springbootlibrary.repository.BookRepository;
import com.cxndytxn.springbootlibrary.repository.ReviewRepository;
import com.cxndytxn.springbootlibrary.requestmodels.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception {

        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, reviewRequest.getBookId());

        if (validateReview != null) {
            throw new Exception("Review already exists.");
        }

        Review review = new Review();
        review.setBookId(reviewRequest.getBookId());
        review.setRating(reviewRequest.getRating());
        review.setUserEmail(userEmail);
        if (reviewRequest.getReviewDescription().isPresent()) {
            review.setReviewDescription(reviewRequest.getReviewDescription().map(
                    Objects::toString
            ).orElse(null));
        }
        review.setDate(Date.valueOf(LocalDate.now()));
        reviewRepository.save(review);
    }

    public Boolean isReviewed(String userEmail, Long bookId) {
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (validateReview != null) {
            return true;
        } else return false;
    }

}
