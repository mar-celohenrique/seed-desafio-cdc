package com.cdc.coupons.controllers;

import com.cdc.coupons.controllers.requests.CouponRequest;
import com.cdc.coupons.entities.Coupon;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
public class CouponController {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/coupons")
    @Transactional
    public ResponseEntity<Coupon> create(@RequestBody @Valid CouponRequest couponRequest) {
        Coupon coupon = couponRequest.toModel();
        this.entityManager.persist(coupon);
        return ResponseEntity.ok(coupon);
    }

}
