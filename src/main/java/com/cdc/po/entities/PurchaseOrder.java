package com.cdc.po.entities;

import com.cdc.commons.validations.Document;
import com.cdc.countries.entities.Country;
import com.cdc.states.entities.State;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotBlank
    @Document
    private String document;

    @NotBlank
    private String address;

    @NotBlank
    private String complement;

    @NotBlank
    private String city;

    @ManyToOne
    @NotNull
    private Country country;

    @ManyToOne
    private State state;

    @NotBlank
    private String phone;

    @NotBlank
    private String zipCode;

    @Deprecated
    public PurchaseOrder() {
    }


    public PurchaseOrder(@NotBlank @Email String email,
                         @NotBlank String name,
                         @NotBlank String lastName,
                         @NotBlank @Document String document,
                         @NotBlank String address,
                         @NotBlank String complement,
                         @NotBlank String city,
                         @NotNull Country country,
                         @NotBlank String phone,
                         @NotBlank String zipCode) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.document = document;
        this.address = address;
        this.complement = complement;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.zipCode = zipCode;
    }

    public void setState(@NotNull @Valid State state) {
        Assert.notNull(this.country, "To associate a state to a country the country must be not null");
        Assert.isTrue(state.belongsToCountry(this.country), "The state does not belong to the country");
        this.state = state;
    }

}
