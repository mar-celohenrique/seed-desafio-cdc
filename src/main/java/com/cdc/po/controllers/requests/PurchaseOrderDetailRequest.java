package com.cdc.po.controllers.requests;

import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class PurchaseOrderDetailRequest {

    @Positive
    @NotNull
    private final BigDecimal total;

    @Size(min = 1)
    @Valid
    @Getter
    private final List<PurchaseOrderDetailItemRequest> itemsRequest;

    public PurchaseOrderDetailRequest(@Positive @NotNull BigDecimal total,
                                      @Size(min = 1) @Valid List<PurchaseOrderDetailItemRequest> itemsRequest) {
        this.total = total;
        this.itemsRequest = itemsRequest;
    }

}
