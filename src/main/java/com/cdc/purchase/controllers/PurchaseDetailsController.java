package com.cdc.purchase.controllers;

import com.cdc.commons.exceptions.EntityNotFoundException;
import com.cdc.purchase.controllers.responses.PurchaseDetailsResponse;
import com.cdc.purchase.entities.Purchase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

@RestController
public class PurchaseDetailsController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/po/{id}")
    @Transactional
    public ResponseEntity<PurchaseDetailsResponse> details(@PathVariable("id") Long id) {
        Purchase purchase = Optional.ofNullable(this.entityManager.find(Purchase.class, id))
                .orElseThrow(() -> new EntityNotFoundException("Purchase [" + id + "] not found!"));

        return ResponseEntity.ok(new PurchaseDetailsResponse(purchase));
    }

}
