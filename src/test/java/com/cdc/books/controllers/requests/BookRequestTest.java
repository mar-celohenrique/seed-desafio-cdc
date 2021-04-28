package com.cdc.books.controllers.requests;

import com.cdc.authors.entities.Author;
import com.cdc.categories.entities.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class BookRequestTest {

    private final BookRequest request = new BookRequest("tite",
            "synopsis", "summary", BigDecimal.valueOf(10L),
            10, "ABC",
            LocalDate.now(), 1L, 1L);

    @Test
    @DisplayName("should not create a book when the category does not exists")
    void toModel1() {
        // given
        EntityManager entityManager = Mockito.mock(EntityManager.class);

        BookRequest request = new BookRequest("tite",
                "synopsis", "summary", BigDecimal.valueOf(10L),
                10, "ABC",
                LocalDate.now(), 1L, 1L);

        // when
        when(entityManager.find(Category.class, 1L)).thenReturn(null);
        when(entityManager.find(Author.class, 1L))
                .thenReturn(new Author("", "", ""));

        // then
        assertThrows(IllegalStateException.class, () -> this.request.toModel(entityManager));
    }

    @Test
    @DisplayName("should not create a book when the author does not exists")
    void toModel2() {
        // given
        EntityManager entityManager = Mockito.mock(EntityManager.class);

        // when
        when(entityManager.find(Category.class, 1L)).thenReturn(new Category(""));
        when(entityManager.find(Author.class, 1L)).thenReturn(null);

        // then
        assertThrows(IllegalStateException.class, () -> this.request.toModel(entityManager));
    }

    @Test
    @DisplayName("should create a book when the author and category exists")
    void toModel3() {
        // given
        EntityManager entityManager = Mockito.mock(EntityManager.class);

        // when
        when(entityManager.find(Category.class, 1L)).thenReturn(new Category(""));
        when(entityManager.find(Author.class, 1L))
                .thenReturn(new Author("", "", ""));

        // then
        assertNotNull(this.request.toModel(entityManager));
    }

}