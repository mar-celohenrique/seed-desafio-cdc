package com.cdc.books.controllers.responses;

import com.cdc.authors.entities.Author;
import lombok.Getter;

@Getter
public class AuthorDetailResponse {

    private final String name;

    private final String description;


    public AuthorDetailResponse(Author author) {
        this.name = author.getName();
        this.description = author.getDescription();
    }

}
