package com.cdc.purchase.entities;

import com.cdc.coupons.entities.Coupon;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Embeddable
@Getter
public class AppliedCoupon {

    @ManyToOne(optional = false)
    private Coupon coupon;

    @Positive
    @NotNull
    private Double discount;

    @NotNull
    @Future
    private LocalDate expirationDate;

    @Deprecated
    public AppliedCoupon() {
    }

    public AppliedCoupon(@Valid @NotNull Coupon coupon) {
        this.coupon = coupon;
        this.discount = coupon.getDiscount();
        this.expirationDate = coupon.getExpirationDate();
    }

}
