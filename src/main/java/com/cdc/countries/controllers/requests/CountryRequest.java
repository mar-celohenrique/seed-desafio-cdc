package com.cdc.countries.controllers.requests;

import com.cdc.commons.validations.UniqueValue;
import com.cdc.countries.entities.Country;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
public class CountryRequest {

    @NotBlank
    @UniqueValue(columnName = "name", domainClass = Country.class)
    private String name;

    public Country toModel() {
        return new Country(this.name);
    }

}
