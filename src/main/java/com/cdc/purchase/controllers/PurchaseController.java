package com.cdc.purchase.controllers;

import com.cdc.commons.validations.CouponValidator;
import com.cdc.commons.validations.StateBelongsCountryValidator;
import com.cdc.coupons.repositories.CouponRepository;
import com.cdc.purchase.controllers.requests.PurchaseRequest;
import com.cdc.purchase.entities.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class PurchaseController {

    private final StateBelongsCountryValidator stateBelongsCountryValidator;

    private final CouponValidator couponValidator;

    private final CouponRepository couponRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(this.stateBelongsCountryValidator, this.couponValidator);
    }

    @PostMapping("/po")
    @Transactional
    public ResponseEntity<Purchase> create(@RequestBody @Valid PurchaseRequest purchaseRequest) {
        Purchase purchase = purchaseRequest.toModel(this.entityManager, this.couponRepository);
        this.entityManager.persist(purchase);
        return ResponseEntity.ok(purchase);
    }

}
