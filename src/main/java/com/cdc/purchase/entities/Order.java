package com.cdc.purchase.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Table(name = "orders")
@Entity
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @NotNull
    @Valid
    @JsonBackReference
    private final Purchase purchase;

    @ElementCollection
    @Size(min = 1)
    private final Set<PurchaseDetailItem> items = new HashSet<>();

    public Order(@NotNull @Valid Purchase purchase,
                 @Size(min = 1) Set<PurchaseDetailItem> items) {
        Assert.isTrue(items.iterator().hasNext(), "One Order must have at least one item");
        this.purchase = purchase;
        this.items.addAll(items);
    }

    public boolean totalEqualsTo(BigDecimal total) {
        BigDecimal totalOrder = this.items.stream()
                .map(PurchaseDetailItem::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalOrder.doubleValue() == total.doubleValue();
    }

}
