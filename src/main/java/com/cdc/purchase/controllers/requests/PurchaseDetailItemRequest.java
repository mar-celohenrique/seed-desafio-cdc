package com.cdc.purchase.controllers.requests;

import com.cdc.books.entities.Book;
import com.cdc.commons.validations.ExistsValue;
import com.cdc.purchase.entities.PurchaseDetailItem;
import lombok.Getter;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PurchaseDetailItemRequest {

    @NotNull
    @ExistsValue(fieldName = "id", domainClass = Book.class)
    @Getter
    private final Long bookId;

    @NotNull
    @Positive
    private final int quantity;

    public PurchaseDetailItemRequest(@NotNull Long bookId, @NotNull @Positive int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public PurchaseDetailItem toModel(EntityManager entityManager) {
        @NotNull Book book = entityManager.find(Book.class, this.bookId);
        return new PurchaseDetailItem(book, this.quantity);
    }

}
