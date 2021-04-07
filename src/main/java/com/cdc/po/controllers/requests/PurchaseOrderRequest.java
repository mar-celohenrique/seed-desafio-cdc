package com.cdc.po.controllers.requests;

import com.cdc.commons.validations.Document;
import com.cdc.commons.validations.ExistsId;
import com.cdc.countries.entities.Country;
import com.cdc.po.entities.PurchaseOrder;
import com.cdc.states.entities.State;
import lombok.Getter;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PurchaseOrderRequest {

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String name;

    @NotBlank
    private final String lastName;

    @NotBlank
    @Document
    private final String document;

    @NotBlank
    private final String address;

    @NotBlank
    private final String complement;

    @NotBlank
    private final String city;

    @NotNull
    @ExistsId(columnName = "id", domainClass = Country.class)
    @Getter
    private final Long countryId;

    @ExistsId(columnName = "id", domainClass = State.class)
    @Getter
    private final Long stateId;

    @NotBlank
    private final String phone;

    @NotBlank
    private final String zipCode;

    @Valid
    @NotNull
    @Getter
    private final PurchaseOrderDetailRequest purchaseOrderDetail;

    public PurchaseOrderRequest(@NotBlank @Email String email,
                                @NotBlank String name,
                                @NotBlank String lastName,
                                @NotBlank @Document String document,
                                @NotBlank String address,
                                @NotBlank String complement,
                                @NotBlank String city,
                                @NotNull Long countryId,
                                Long stateId,
                                @NotBlank String phone,
                                @NotBlank String zipCode,
                                @Valid PurchaseOrderDetailRequest purchaseOrderDetail) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.document = document;
        this.address = address;
        this.complement = complement;
        this.city = city;
        this.countryId = countryId;
        this.stateId = stateId;
        this.phone = phone;
        this.zipCode = zipCode;
        this.purchaseOrderDetail = purchaseOrderDetail;
    }

    public PurchaseOrder toModel(EntityManager entityManager) {
        @NotNull Country country = entityManager.find(Country.class, this.countryId);

        PurchaseOrder purchaseOrder = new PurchaseOrder(
                this.email,
                this.name,
                this.lastName,
                this.document,
                this.address,
                this.complement,
                this.city,
                country,
                this.phone,
                this.zipCode);

        if (this.stateId != null) {
            purchaseOrder.setState(entityManager.find(State.class, this.stateId));
        }

        return purchaseOrder;
    }

    public boolean hasState() {
        return this.stateId != null;
    }

}
