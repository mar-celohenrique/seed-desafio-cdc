package com.cdc.authors.controllers.requests;

import com.cdc.authors.entities.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class AuthorRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(max = 400)
    private String description;


    public Author toModel() {
        return new Author(this.name, this.email, this.description);
    }

}
