package com.cxndytxn.springbootlibrary.requestmodels;

import lombok.Data;

import java.util.Optional;

@Data
public class ReviewRequest {

    private Double rating;

    private Long bookId;

    private Optional<String> reviewDescription;

}
