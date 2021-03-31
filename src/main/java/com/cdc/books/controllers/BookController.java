package com.cdc.books.controllers;

import com.cdc.books.controllers.requests.BookRequest;
import com.cdc.books.entities.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
public class BookController {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/books")
    @Transactional
    public ResponseEntity<Book> create(@Valid @RequestBody BookRequest bookRequest) {
        Book book = bookRequest.toModel(this.entityManager);
        this.entityManager.persist(book);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/books")
    @Transactional
    public ResponseEntity<List<Book>> list() {
        List<Book> books = this.entityManager.createQuery("from " + Book.class.getName(), Book.class).getResultList();
        return ResponseEntity.ok(books);
    }

}
