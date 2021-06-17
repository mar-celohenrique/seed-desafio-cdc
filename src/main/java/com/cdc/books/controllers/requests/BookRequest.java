package com.cdc.books.controllers.requests;

import com.cdc.authors.entities.Author;
import com.cdc.books.entities.Book;
import com.cdc.categories.entities.Category;
import com.cdc.commons.validations.ExistsValue;
import com.cdc.commons.validations.UniqueValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
public class BookRequest {

    @NotBlank
    @UniqueValue(columnName = "title", domainClass = Book.class)
    private String title;

    @NotBlank
    @Length(max = 500)
    private String synopsis;

    private String summary;

    @NotNull
    @Min(value = 20)
    private BigDecimal price;

    @NotNull
    @Min(value = 100)
    private int pages;

    @NotBlank
    @UniqueValue(columnName = "isbn", domainClass = Book.class)
    private String isbn;

    @Future
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate publicationDate;

    @NotNull
    @ExistsValue(fieldName = "id", domainClass = Category.class)
    private Long categoryId;

    @NotNull
    @ExistsValue(fieldName = "id", domainClass = Author.class)
    private Long authorId;

    public BookRequest(@NotBlank String title,
                       @NotBlank @Length(max = 500) String synopsis,
                       String summary,
                       @NotNull @Min(value = 20) BigDecimal price,
                       @NotNull @Min(value = 100) int pages,
                       @NotBlank String isbn,
                       @Future @NotNull LocalDate publicationDate,
                       @NotNull Long categoryId,
                       @NotNull Long authorId) {
        this.title = title;
        this.synopsis = synopsis;
        this.summary = summary;
        this.price = price;
        this.pages = pages;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.categoryId = categoryId;
        this.authorId = authorId;
    }

    public Book toModel(EntityManager entityManager) {
        @NotNull Category category = entityManager.find(Category.class, this.categoryId);
        @NotNull Author author = entityManager.find(Author.class, this.authorId);

        Assert.state(category != null, "The category must exists to create a book.");
        Assert.state(author != null, "The author must exists to create a book.");

        return new Book(this.title,
                this.synopsis,
                this.summary,
                this.price,
                this.pages,
                this.isbn,
                this.publicationDate,
                category,
                author);
    }

}
