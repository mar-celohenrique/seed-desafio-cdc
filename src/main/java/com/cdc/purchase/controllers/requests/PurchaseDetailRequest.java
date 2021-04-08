package com.cdc.purchase.controllers.requests;

import com.cdc.purchase.entities.Order;
import com.cdc.purchase.entities.Purchase;
import com.cdc.purchase.entities.PurchaseDetailItem;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PurchaseDetailRequest {

    @Positive
    @NotNull
    private final BigDecimal total;

    @Size(min = 1)
    @Valid
    @Getter
    private final List<PurchaseDetailItemRequest> itemsRequest;

    public PurchaseDetailRequest(@Positive @NotNull BigDecimal total,
                                 @Size(min = 1) @Valid List<PurchaseDetailItemRequest> itemsRequest) {
        this.total = total;
        this.itemsRequest = itemsRequest;
    }

    public Function<Purchase, Order> toModel(EntityManager entityManager) {
        Set<PurchaseDetailItem> purchaseDetailItems = this.itemsRequest.stream()
                .map(item -> item.toModel(entityManager))
                .collect(Collectors.toSet());

        return (purchase) -> {
            Order order = new Order(purchase, purchaseDetailItems);
            Assert.isTrue(order.totalEqualsTo(this.total), "The total that was sent does not match with the real total");
            return order;
        };
    }

}
