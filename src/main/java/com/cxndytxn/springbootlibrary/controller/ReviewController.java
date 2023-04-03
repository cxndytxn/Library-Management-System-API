package com.cxndytxn.springbootlibrary.controller;

import com.cxndytxn.springbootlibrary.requestmodels.ReviewRequest;
import com.cxndytxn.springbootlibrary.service.ReviewService;
import com.cxndytxn.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/secure")
    public void postReview(@RequestHeader(value = "Authorization") String token, @RequestBody ReviewRequest reviewRequest) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");

        if (userEmail == null) {
            throw new Exception("User email is missing!");
        }

        reviewService.postReview(userEmail, reviewRequest);
    }

    @GetMapping("/secure/is-reviewed")
    public Boolean isReviewed(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");

        if (userEmail == null) {
            throw new Exception("User email is missing!");
        }

        return reviewService.isReviewed(userEmail, bookId);
    }

}
