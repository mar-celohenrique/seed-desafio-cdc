package com.cdc.commons.validations;

import com.cdc.coupons.entities.Coupon;
import com.cdc.coupons.repositories.CouponRepository;
import com.cdc.purchase.controllers.requests.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CouponValidator implements Validator {

    private final CouponRepository couponRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return PurchaseRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        PurchaseRequest purchaseRequest = (PurchaseRequest) target;
        Optional<String> couponCode = purchaseRequest.getCouponCode();

        if (couponCode.isPresent()) {
            Coupon coupon = this.couponRepository.getByCode(couponCode.get());
            if (coupon == null || !coupon.isValid()) {
                errors.rejectValue("couponCode", null, "This coupon is not valid");
            }
        }
    }

}
