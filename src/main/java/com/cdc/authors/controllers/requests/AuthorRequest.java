package com.cdc.authors.controllers.requests;

import com.cdc.authors.entities.Author;
import com.cdc.commons.validations.UniqueValue;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AuthorRequest {

    @NotBlank
    private final String name;

    @NotBlank
    @Email
    @UniqueValue(columnName = "email", domainClass = Author.class)
    private final String email;

    @NotBlank
    @Length(max = 400)
    private final String description;

    public AuthorRequest(@NotBlank String name,
                         @NotBlank @Email String email,
                         @NotBlank @Length(max = 400) String description) {
        this.name = name;
        this.email = email;
        this.description = description;
    }

    public Author toModel() {
        return new Author(this.name, this.email, this.description);
    }

}
