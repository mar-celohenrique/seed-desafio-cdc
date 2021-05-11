package com.cdc.commons.validations;

import com.cdc.coupons.entities.Coupon;
import com.cdc.coupons.repositories.CouponRepository;
import com.cdc.purchase.controllers.requests.PurchaseDetailItemRequest;
import com.cdc.purchase.controllers.requests.PurchaseDetailRequest;
import com.cdc.purchase.controllers.requests.PurchaseRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class CouponValidatorTest {

    private final CouponRepository couponRepository = Mockito.mock(CouponRepository.class);

    private final PurchaseRequest request;

    {
        ArrayList<PurchaseDetailItemRequest> itemsRequest = new ArrayList<>();
        PurchaseDetailItemRequest detailItemRequest = new PurchaseDetailItemRequest(1L, 1);
        itemsRequest.add(detailItemRequest);
        PurchaseDetailRequest purchaseOrderDetail = new PurchaseDetailRequest(BigDecimal.TEN, itemsRequest);
        request = new PurchaseRequest("email",
                "name",
                "lastname",
                "document",
                "address",
                "complement",
                "city", 1L, "phone", "zipCode", purchaseOrderDetail);
    }

    @Test
    @DisplayName("should reject with a invalid coupon")
    void validate1() {
        // given
        Coupon coupon = new Coupon("code", BigDecimal.TEN, LocalDate.now());
        ReflectionTestUtils.setField(coupon, "expirationDate", LocalDate.now().minusDays(1));

        this.request.setCouponCode("code");

        Errors errors = new BeanPropertyBindingResult(this.request, "target");
        CouponValidator validator = new CouponValidator(this.couponRepository);

        // when
        when(this.couponRepository.getByCode("code")).thenReturn(coupon);

        // then
        validator.validate(this.request, errors);

        assertEquals(errors.getAllErrors().size(), 1);
        assertNotNull(errors.getFieldError("couponCode"));
    }

    @Test
    @DisplayName("should not reject with a valid coupon")
    void validate2() {
        // given
        Coupon coupon = new Coupon("code", BigDecimal.TEN, LocalDate.now());

        this.request.setCouponCode("code");

        Errors errors = new BeanPropertyBindingResult(this.request, "target");
        CouponValidator validator = new CouponValidator(this.couponRepository);

        // when
        when(this.couponRepository.getByCode("code")).thenReturn(coupon);

        // then
        validator.validate(this.request, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    @DisplayName("should throws IllegalStateException when the coupon does not exists")
    void validate3() {
        // given
        this.request.setCouponCode("code");
        Errors errors = new BeanPropertyBindingResult(this.request, "target");
        CouponValidator validator = new CouponValidator(this.couponRepository);

        // when
        when(this.couponRepository.getByCode("code")).thenReturn(null);

        // then
        assertThrows(IllegalStateException.class, () -> validator.validate(this.request, errors));
    }

    @Test
    @DisplayName("should not call the repository when the coupon code is not present")
    void validate4() {
        // given
        Errors errors = new BeanPropertyBindingResult(this.request, "target");
        CouponValidator validator = new CouponValidator(this.couponRepository);

        // then
        validator.validate(this.request, errors);

        assertFalse(errors.hasErrors());
        verifyNoMoreInteractions(this.couponRepository);
    }

    @Test
    @DisplayName("should stop if already has errors")
    void validate5() {
        // given
        Errors errors = new BeanPropertyBindingResult(this.request, "target");
        errors.reject("couponCode");

        CouponValidator validator = new CouponValidator(this.couponRepository);

        // then
        validator.validate(this.request, errors);

        assertEquals(errors.getAllErrors().size(), 1);
        assertEquals("couponCode", errors.getGlobalErrors().get(0).getCode());
    }

}