package com.cxndytxn.springbootlibrary.responsemodels;

import com.cxndytxn.springbootlibrary.entity.Book;
import lombok.Data;

@Data
public class LoanResponse {

    public LoanResponse(Book book, int daysLeft) {
        this.book = book;
        this.daysLeft = daysLeft;
    }

    private Book book;

    private int daysLeft;

}
