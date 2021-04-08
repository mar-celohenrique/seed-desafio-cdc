package com.cdc.coupons.entities;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Getter
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    @NotBlank
    private String code;

    @Column(name = "discount", nullable = false)
    @Positive
    @NotNull
    private Double discount;

    @Column(name = "expiration_date", nullable = false)
    @NotNull
    @Future
    private LocalDate expirationDate;

    @Deprecated
    public Coupon() {
    }

    public Coupon(@NotBlank String code,
                  @NotNull @Positive Double discount,
                  @NotNull @Future LocalDate expirationDate) {
        this.code = code;
        this.discount = discount;
        this.expirationDate = expirationDate;
    }

    public boolean isValid() {
        return LocalDate.now().compareTo(this.expirationDate) <= 0;
    }

}
