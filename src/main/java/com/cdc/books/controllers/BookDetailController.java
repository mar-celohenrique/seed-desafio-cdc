package com.cdc.books.controllers;

import com.cdc.books.controllers.responses.BookDetailResponse;
import com.cdc.books.entities.Book;
import com.cdc.commons.exceptions.EntityNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@RestController
public class BookDetailController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/books/{id}")
    private BookDetailResponse book(@PathVariable("id") Long id) {
        Book book = Optional.ofNullable(this.entityManager.find(Book.class, id))
                .orElseThrow(() -> new EntityNotFoundException("Book [" + id + "] not found!"));

        return new BookDetailResponse(book);
    }

}
