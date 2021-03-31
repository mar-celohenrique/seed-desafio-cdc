package com.cdc.books.controllers.responses;

import com.cdc.books.entities.Book;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class BookDetailResponse {

    private final String title;

    private final String isbn;

    private final int pages;

    private final BigDecimal price;

    private final String synopsis;

    private final String summary;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private final LocalDate publicationDate;

    private final AuthorDetailResponse author;

    public BookDetailResponse(Book book) {
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
        this.pages = book.getPages();
        this.price = book.getPrice();
        this.synopsis = book.getSynopsis();
        this.summary = book.getSummary();
        this.publicationDate = book.getPublicationDate();
        this.author = new AuthorDetailResponse(book.getAuthor());
    }

}
