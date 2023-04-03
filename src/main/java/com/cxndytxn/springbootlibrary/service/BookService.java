package com.cxndytxn.springbootlibrary.service;

import com.cxndytxn.springbootlibrary.entity.Book;
import com.cxndytxn.springbootlibrary.entity.Checkout;
import com.cxndytxn.springbootlibrary.repository.BookRepository;
import com.cxndytxn.springbootlibrary.repository.CheckoutRepository;
import com.cxndytxn.springbootlibrary.responsemodels.LoanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CheckoutRepository checkoutRepository;

    public Book checkoutBook(String userEmail, Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (book.isEmpty() || validateCheckout != null || book.get().getCopiesAvailable() <= 0) {
            throw new Exception("Book doesn't exist or has already been checked out by user.");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        bookRepository.save(book.get());

        Checkout checkout = new Checkout(userEmail, LocalDate.now().toString(), LocalDate.now().plusDays(7).toString(), book.get().getId());

        checkoutRepository.save(checkout);

        return book.get();
    }

    public Boolean isBookCheckedOut(String userEmail, Long bookId) {

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        return validateCheckout != null;
    }

    public int loanCount(String userEmail) {

        return checkoutRepository.findBooksByUserEmail(userEmail).size();

    }

    public List<LoanResponse> loans(String userEmail) throws Exception {
        List<LoanResponse> loanResponses = new ArrayList<>();

        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);
        List<Long> bookIdList = new ArrayList<>();

        for (Checkout i: checkoutList) {
            bookIdList.add(i.getBookId());
        }

        List<Book> books = bookRepository.findBooksByBookIds(bookIdList);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Book book: books) {
            Optional<Checkout> checkout = checkoutList.stream().filter(x -> Objects.equals(x.getBookId(), book.getId())).findFirst();
            if (checkout.isPresent()) {
                Date d1 = simpleDateFormat.parse(checkout.get().getReturnDate());
                Date d2 = simpleDateFormat.parse(LocalDate.now().toString());

                TimeUnit timeUnit = TimeUnit.DAYS;

                long differenceInTime = timeUnit.convert(d1.getTime() - d2.getTime(), TimeUnit.MILLISECONDS);

                loanResponses.add(new LoanResponse(book, (int) differenceInTime));
            }
        }

        return loanResponses;
    }
}
