package com.cdc.coupons.controllers.requests;

import com.cdc.commons.validations.UniqueValue;
import com.cdc.coupons.entities.Coupon;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CouponRequest {

    @NotBlank
    @UniqueValue(columnName = "code", domainClass = Coupon.class)
    private final String code;

    @NotNull
    @Positive
    private final BigDecimal discount;

    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private final LocalDate expirationDate;

    public CouponRequest(@NotBlank String code,
                         @NotNull @Positive BigDecimal discount,
                         @NotNull @FutureOrPresent LocalDate expirationDate) {
        this.code = code;
        this.discount = discount;
        this.expirationDate = expirationDate;
    }

    public Coupon toModel() {
        return new Coupon(this.code, this.discount, this.expirationDate);
    }

}
