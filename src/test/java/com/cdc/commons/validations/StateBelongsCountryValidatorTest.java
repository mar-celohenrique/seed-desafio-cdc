package com.cdc.commons.validations;

import com.cdc.countries.entities.Country;
import com.cdc.purchase.controllers.requests.PurchaseDetailItemRequest;
import com.cdc.purchase.controllers.requests.PurchaseDetailRequest;
import com.cdc.purchase.controllers.requests.PurchaseRequest;
import com.cdc.states.entities.State;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class StateBelongsCountryValidatorTest {

    private final EntityManager entityManager = Mockito.mock(EntityManager.class);

    private final Country country = new Country("country");

    private final State state = new State("state", this.country);

    private final PurchaseRequest request;

    {
        ArrayList<PurchaseDetailItemRequest> itemsRequest = new ArrayList<>();
        PurchaseDetailItemRequest detailItemRequest = new PurchaseDetailItemRequest(1L, 1);
        itemsRequest.add(detailItemRequest);
        PurchaseDetailRequest purchaseOrderDetail = new PurchaseDetailRequest(BigDecimal.TEN, itemsRequest);
        request = new PurchaseRequest("email",
                "name",
                "lastname",
                "document",
                "address",
                "complement",
                "city", 1L, "phone", "zipCode", purchaseOrderDetail);
    }

    @Test
    @DisplayName("should reject when the state does not belongs the country")
    void validate1() {
        // given
        this.request.setStateId(1L);

        Errors errors = new BeanPropertyBindingResult(this.request, "target");
        StateBelongsCountryValidator validator = new StateBelongsCountryValidator();
        ReflectionTestUtils.setField(validator, "entityManager", this.entityManager);

        // when
        when(this.entityManager.find(Country.class, 1L)).thenReturn(new Country("new country"));
        when(this.entityManager.find(State.class, 1L)).thenReturn(this.state);

        // then
        validator.validate(this.request, errors);

        assertEquals(errors.getAllErrors().size(), 1);
        assertNotNull(errors.getFieldError("countryId"));
    }

    @Test
    @DisplayName("should not reject when the state belongs the country")
    void validate2() {
        // given
        this.request.setStateId(1L);

        Errors errors = new BeanPropertyBindingResult(this.request, "target");
        StateBelongsCountryValidator validator = new StateBelongsCountryValidator();
        ReflectionTestUtils.setField(validator, "entityManager", this.entityManager);

        // when
        when(this.entityManager.find(Country.class, 1L)).thenReturn(this.country);
        when(this.entityManager.find(State.class, 1L)).thenReturn(this.state);

        // then
        validator.validate(this.request, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    @DisplayName("should throws IllegalStateException when the country does not exists")
    void validate3() {
        // given
        this.request.setStateId(1L);
        Errors errors = new BeanPropertyBindingResult(this.request, "target");
        StateBelongsCountryValidator validator = new StateBelongsCountryValidator();
        ReflectionTestUtils.setField(validator, "entityManager", this.entityManager);

        // when
        when(this.entityManager.find(Country.class, 1L)).thenReturn(null);
        when(this.entityManager.find(State.class, 1L)).thenReturn(this.state);

        // then
        assertThrows(IllegalStateException.class, () -> validator.validate(this.request, errors));
    }

    @Test
    @DisplayName("should throws IllegalStateException when the state does not exists")
    void validate4() {
        // given
        this.request.setStateId(1L);
        Errors errors = new BeanPropertyBindingResult(this.request, "target");
        StateBelongsCountryValidator validator = new StateBelongsCountryValidator();
        ReflectionTestUtils.setField(validator, "entityManager", this.entityManager);

        // when
        when(this.entityManager.find(Country.class, 1L)).thenReturn(this.country);
        when(this.entityManager.find(State.class, 1L)).thenReturn(null);

        // then
        assertThrows(IllegalStateException.class, () -> validator.validate(this.request, errors));
    }

    @Test
    @DisplayName("should not call the entity manager when the state id is not present")
    void validate5() {
        // given
        Errors errors = new BeanPropertyBindingResult(this.request, "target");
        StateBelongsCountryValidator validator = new StateBelongsCountryValidator();
        ReflectionTestUtils.setField(validator, "entityManager", this.entityManager);

        // then
        validator.validate(this.request, errors);

        assertFalse(errors.hasErrors());
        verifyNoMoreInteractions(this.entityManager);
    }

    @Test
    @DisplayName("should stop if already has errors")
    void validate6() {
        // given
        Errors errors = new BeanPropertyBindingResult(this.request, "target");
        errors.reject("countryId");

        StateBelongsCountryValidator validator = new StateBelongsCountryValidator();
        ReflectionTestUtils.setField(validator, "entityManager", this.entityManager);

        // then
        validator.validate(this.request, errors);

        assertEquals(errors.getAllErrors().size(), 1);
        assertEquals("countryId", errors.getGlobalErrors().get(0).getCode());
    }

}