package com.cdc.commons.validations;

import com.cdc.countries.entities.Country;
import com.cdc.po.controllers.requests.PurchaseOrderRequest;
import com.cdc.states.entities.State;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class StateBelongsCountryValidator implements Validator {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean supports(Class<?> clazz) {
        return PurchaseOrderRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        PurchaseOrderRequest purchaseOrderRequest = (PurchaseOrderRequest) target;

        if (purchaseOrderRequest.hasState()) {
            Country country = this.entityManager.find(Country.class, purchaseOrderRequest.getCountryId());
            State state = this.entityManager.find(State.class, purchaseOrderRequest.getStateId());
            if (!state.belongsToCountry(country)) {
                errors.reject("countryId", null, "The state does not belong to the country");
            }
        }
    }

}
