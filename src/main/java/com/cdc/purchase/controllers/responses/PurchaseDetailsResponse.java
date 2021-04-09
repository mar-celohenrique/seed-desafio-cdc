package com.cdc.purchase.controllers.responses;

import com.cdc.purchase.entities.Purchase;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class PurchaseDetailsResponse {

    private final String email;

    private final String address;

    private final BigDecimal total;

    private final boolean hasCoupon;

    private final BigDecimal totalFinal;

    public PurchaseDetailsResponse(Purchase purchase) {
        this.email = purchase.getEmail();
        this.address = purchase.getAddressFormatted();
        this.total = purchase.getOrder().getTotal();
        this.hasCoupon = purchase.hasCoupon();
        this.totalFinal = this.hasCoupon ?
                this.total.subtract(this.total.multiply(purchase
                        .getAppliedCoupon()
                        .getDiscount()
                        .divide(new BigDecimal(100), RoundingMode.HALF_UP))) : null;
    }

}
