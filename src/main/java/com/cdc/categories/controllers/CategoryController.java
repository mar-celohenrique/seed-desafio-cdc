package com.cdc.categories.controllers;

import com.cdc.categories.controllers.requests.CategoryRequest;
import com.cdc.categories.entities.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
public class CategoryController {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/categories")
    @Transactional
    public ResponseEntity<Category> create(@Valid @RequestBody CategoryRequest categoryRequest) {
        Category category = categoryRequest.toModel();
        this.entityManager.persist(category);
        return ResponseEntity.ok(category);
    }

}
