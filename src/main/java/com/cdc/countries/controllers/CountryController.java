package com.cdc.countries.controllers;

import com.cdc.countries.controllers.requests.CountryRequest;
import com.cdc.countries.entities.Country;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
public class CountryController {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/countries")
    @Transactional
    public ResponseEntity<Country> create(@RequestBody @Valid CountryRequest countryRequest) {
        Country country = countryRequest.toModel();
        this.entityManager.persist(country);
        return ResponseEntity.ok(country);
    }

}
