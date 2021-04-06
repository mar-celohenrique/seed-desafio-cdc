package com.cdc.states.controllers;

import com.cdc.states.controllers.requests.StateRequest;
import com.cdc.states.entities.State;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
public class StateController {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/states")
    @Transactional
    public ResponseEntity<State> create(@RequestBody @Valid StateRequest stateRequest) {
        State state = stateRequest.toModel(this.entityManager);
        this.entityManager.persist(state);
        return ResponseEntity.ok(state);
    }

}
