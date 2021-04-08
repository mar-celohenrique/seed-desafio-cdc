package com.cdc.books.entities;

import com.cdc.authors.entities.Author;
import com.cdc.categories.entities.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"isbn"})
public class Book implements Serializable {

    private static final long serialVersionUID = -1775286647702244226L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    @NotBlank
    private String title;

    @Column(name = "synopsis", nullable = false, length = 500, columnDefinition = "TEXT")
    @NotBlank
    @Length(max = 500)
    private String synopsis;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "price", nullable = false)
    @NotNull
    @Min(value = 20)
    private BigDecimal price;

    @Column(name = "pages", nullable = false)
    @NotNull
    @Min(value = 100)
    private int pages;

    @Column(name = "isbn", nullable = false, unique = true)
    @NotBlank
    private String isbn;

    @Column(name = "publication_date")
    @Future
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate publicationDate;

    @ManyToOne(optional = false)
    @NotNull
    private Category category;

    @ManyToOne(optional = false)
    @NotNull
    private Author author;

    @Deprecated
    public Book() {
    }

    public Book(@NotBlank String title,
                @NotBlank @Length(max = 500) String synopsis,
                String summary,
                @NotNull @Min(value = 20) BigDecimal price,
                @NotNull @Min(value = 100) int pages,
                @NotBlank String isbn,
                @Future @NotNull LocalDate publicationDate,
                @Valid @NotNull Category category,
                @Valid @NotNull Author author) {

        this.title = title;
        this.synopsis = synopsis;
        this.summary = summary;
        this.price = price;
        this.pages = pages;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.category = category;
        this.author = author;
    }

}
