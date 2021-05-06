package com.cdc.purchase.entities;

import com.cdc.authors.entities.Author;
import com.cdc.books.entities.Book;
import com.cdc.categories.entities.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {

    @ParameterizedTest
    @DisplayName("verify if the total of order is the same passed as argument")
    @CsvSource({
            "10, true", "9.99, false", "10.01,false"
    })
    void totalEqualsTo(BigDecimal price, boolean expectedValue) {
        // given
        Author author = new Author("name", "email", "description");
        Category category = new Category("category");
        Book book = new Book("tite",
                "synopsis", "summary", price,
                10, "ABC",
                LocalDate.now(), category, author);

        Set<PurchaseDetailItem> items = Set.of(new PurchaseDetailItem(book, 1));
        Order order = new Order(Mockito.mock(Purchase.class), items);

        // when
        boolean result = order.totalEqualsTo(BigDecimal.TEN);

        // then
        assertEquals(result, expectedValue);
    }

}