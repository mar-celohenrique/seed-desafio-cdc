package com.cdc.po.controllers.requests;

import com.cdc.books.entities.Book;
import com.cdc.commons.validations.ExistsId;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class PurchaseOrderDetailItemRequest {

    @NotNull
    @ExistsId(columnName = "id", domainClass = Book.class)
    @Getter
    private final Long id;

    @NotNull
    @Positive
    private final int quantity;

    public PurchaseOrderDetailItemRequest(@NotNull Long id, @NotNull @Positive int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

}
