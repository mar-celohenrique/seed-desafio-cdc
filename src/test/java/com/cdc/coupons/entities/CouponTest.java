package com.cdc.coupons.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.ReflectionTestUtils.setField;

class CouponTest {

    @ParameterizedTest
    @CsvSource({
            "0, true",
            "1, true"
    })
    @DisplayName("the coupon is valid when the expiration date is not on the past")
    void isValid1(long days, boolean expectedResult) {
        // given
        Coupon coupon = new Coupon("code", BigDecimal.ONE, LocalDate.now().plusDays(days));

        // when
        boolean isValid = coupon.isValid();

        // then
        assertEquals(isValid, expectedResult);
    }

    @Test
    @DisplayName("should not create the a coupon when the expiration date is on the past")
    void isValid2() {
        // given
        LocalDate expirationDate = LocalDate.now().minusDays(1L);

        // then
        assertThrows(IllegalArgumentException.class, () -> new Coupon("code", BigDecimal.ONE, expirationDate));
    }

    @Test
    @DisplayName("the coupon is not valid when the expiration date is on the past")
    void isValid3() {
        // given
        Coupon coupon = new Coupon("code", BigDecimal.ONE, LocalDate.now());
        setField(coupon, "expirationDate", LocalDate.now().minusDays(1L));

        // when
        boolean isValid = coupon.isValid();

        // then
        assertFalse(isValid);
    }

}