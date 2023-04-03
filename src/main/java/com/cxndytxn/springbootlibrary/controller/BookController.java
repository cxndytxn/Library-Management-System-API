package com.cxndytxn.springbootlibrary.controller;

import com.cxndytxn.springbootlibrary.entity.Book;
import com.cxndytxn.springbootlibrary.responsemodels.LoanResponse;
import com.cxndytxn.springbootlibrary.service.BookService;
import com.cxndytxn.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/secure/loans")
    public List<LoanResponse> loans(@RequestHeader(value = "Authorization") String token) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return bookService.loans(userEmail);
    }

    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestParam Long bookId, @RequestHeader(value = "Authorization") String token) throws Exception {

        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");

        return bookService.checkoutBook(userEmail, bookId);
    }

    @GetMapping("/secure/is-book-checked-out")
    public Boolean isBookCheckedOut(@RequestParam Long bookId, @RequestHeader(value = "Authorization") String token) {

        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");

        return bookService.isBookCheckedOut(userEmail, bookId);
    }

    @GetMapping("/secure/loan-count")
    public int loanCount(@RequestHeader(value = "Authorization") String token) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");

        return bookService.loanCount(userEmail);
    }
}
