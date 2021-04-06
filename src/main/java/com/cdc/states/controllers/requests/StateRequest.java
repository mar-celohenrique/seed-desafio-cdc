package com.cdc.states.controllers.requests;

import com.cdc.commons.validations.ExistsId;
import com.cdc.commons.validations.UniqueValue;
import com.cdc.countries.entities.Country;
import com.cdc.states.entities.State;
import lombok.Setter;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
public class StateRequest {

    @NotBlank
    @UniqueValue(columnName = "name", domainClass = State.class)
    private final String name;

    @ExistsId(columnName = "id", domainClass = Country.class)
    @NotNull
    private final Long countryId;

    public StateRequest(@NotBlank String name, @NotNull Long countryId) {
        this.name = name;
        this.countryId = countryId;
    }

    public State toModel(EntityManager entityManager) {
        @NotNull Country country = entityManager.find(Country.class, this.countryId);
        return new State(this.name, country);
    }

}
