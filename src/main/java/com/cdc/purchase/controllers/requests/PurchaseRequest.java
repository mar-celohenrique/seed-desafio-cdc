package com.cdc.purchase.controllers.requests;

import com.cdc.commons.validations.Document;
import com.cdc.commons.validations.ExistsValue;
import com.cdc.countries.entities.Country;
import com.cdc.coupons.entities.Coupon;
import com.cdc.coupons.repositories.CouponRepository;
import com.cdc.purchase.entities.Order;
import com.cdc.purchase.entities.Purchase;
import com.cdc.states.entities.State;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.Function;

public class PurchaseRequest {

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String name;

    @NotBlank
    private final String lastName;

    @NotBlank
    @Document
    private final String document;

    @NotBlank
    private final String address;

    @NotBlank
    private final String complement;

    @NotBlank
    private final String city;

    @NotNull
    @ExistsValue(fieldName = "id", domainClass = Country.class)
    @Getter
    private final Long countryId;

    @ExistsValue(fieldName = "id", domainClass = State.class)
    @Getter
    @Setter
    private Long stateId;

    @NotBlank
    private final String phone;

    @NotBlank
    private final String zipCode;

    @Valid
    @NotNull
    @Getter
    private final PurchaseDetailRequest purchaseOrderDetail;

    @Setter
    @ExistsValue(fieldName = "code", domainClass = Coupon.class)
    private String couponCode;

    public PurchaseRequest(@NotBlank @Email String email,
                           @NotBlank String name,
                           @NotBlank String lastName,
                           @NotBlank @Document String document,
                           @NotBlank String address,
                           @NotBlank String complement,
                           @NotBlank String city,
                           @NotNull Long countryId,
                           @NotBlank String phone,
                           @NotBlank String zipCode,
                           @Valid PurchaseDetailRequest purchaseOrderDetail) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.document = document;
        this.address = address;
        this.complement = complement;
        this.city = city;
        this.countryId = countryId;
        this.phone = phone;
        this.zipCode = zipCode;
        this.purchaseOrderDetail = purchaseOrderDetail;
    }

    public Purchase toModel(EntityManager entityManager, CouponRepository couponRepository) {
        @NotNull Country country = entityManager.find(Country.class, this.countryId);

        Function<Purchase, Order> createOrderFunction =
                this.purchaseOrderDetail.toModel(entityManager);

        Purchase purchase = new Purchase(
                this.email,
                this.name,
                this.lastName,
                this.document,
                this.address,
                this.complement,
                this.city,
                country,
                this.phone,
                this.zipCode,
                createOrderFunction);

        if (this.hasState()) {
            purchase.setState(entityManager.find(State.class, this.stateId));
        }

        if (StringUtils.hasText(this.couponCode)) {
            Coupon coupon = couponRepository.getByCode(this.couponCode);
            purchase.applyCoupon(coupon);
        }

        return purchase;
    }

    public Optional<String> getCouponCode() {
        return Optional.ofNullable(this.couponCode);
    }

    public boolean hasState() {
        return Optional.ofNullable(this.stateId).isPresent();
    }

}
