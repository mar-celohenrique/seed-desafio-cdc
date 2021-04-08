package com.cdc.purchase.entities;

import com.cdc.books.entities.Book;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Embeddable
@EqualsAndHashCode(of = {"book"})
@Getter
public class PurchaseDetailItem {

    @NotNull
    @ManyToOne(optional = false)
    private Book book;

    @NotNull
    @Positive
    private int quantity;

    @Positive
    private BigDecimal bookPrice;

    @Deprecated
    public PurchaseDetailItem() {
    }

    public PurchaseDetailItem(@NotNull Book book, @NotNull @Positive int quantity) {
        this.book = book;
        this.quantity = quantity;
        this.bookPrice = book.getPrice();
    }

    public BigDecimal total() {
        return this.bookPrice.multiply(new BigDecimal(this.quantity));
    }

}
