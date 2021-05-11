package com.cdc.commons.validations;

import com.cdc.countries.entities.Country;
import com.cdc.purchase.controllers.requests.PurchaseRequest;
import com.cdc.states.entities.State;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Objects;

@Component
public class StateBelongsCountryValidator implements Validator {

    @PersistenceContext
    private EntityManager entityManager;

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

        if (purchaseRequest.hasState()) {
            Country country = this.entityManager.find(Country.class, purchaseRequest.getCountryId());
            State state = this.entityManager.find(State.class, purchaseRequest.getStateId());
            Assert.state(Objects.nonNull(country), "The country must exists to be validated");
            Assert.state(Objects.nonNull(state), "The state must exists to be validated");
            if (!state.belongsToCountry(country)) {
                errors.rejectValue("countryId", null, "The state does not belong to the country");
            }
        }
    }

}
