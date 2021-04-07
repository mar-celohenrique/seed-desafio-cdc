package com.cdc.po.controllers;

import com.cdc.commons.validations.StateBelongsCountryValidator;
import com.cdc.po.controllers.requests.PurchaseOrderRequest;
import com.cdc.po.entities.PurchaseOrder;
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
public class PurchaseOrderController {

    private final StateBelongsCountryValidator stateBelongsCountryValidator;

    @PersistenceContext
    private EntityManager entityManager;

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.addValidators(this.stateBelongsCountryValidator);
    }

    @PostMapping("/po")
    @Transactional
    public ResponseEntity<PurchaseOrder> create(@RequestBody @Valid PurchaseOrderRequest purchaseOrderRequest) {
        PurchaseOrder purchaseOrder = purchaseOrderRequest.toModel(this.entityManager);
        return ResponseEntity.ok(purchaseOrder);
    }

}
