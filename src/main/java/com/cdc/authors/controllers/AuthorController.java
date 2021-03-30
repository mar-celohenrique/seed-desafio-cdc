package com.cdc.authors.controllers;

import com.cdc.authors.controllers.requests.AuthorRequest;
import com.cdc.authors.entities.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/authors")
    @Transactional
    public ResponseEntity<Author> create(@Valid @RequestBody AuthorRequest authorRequest) {
        Author author = authorRequest.toModel();
        this.entityManager.persist(author);
        return ResponseEntity.ok(author);
    }

}
