package com.cdc.purchase.controllers.requests;

import com.cdc.authors.entities.Author;
import com.cdc.books.entities.Book;
import com.cdc.categories.entities.Category;
import com.cdc.countries.entities.Country;
import com.cdc.coupons.entities.Coupon;
import com.cdc.coupons.repositories.CouponRepository;
import com.cdc.purchase.entities.Purchase;
import com.cdc.states.entities.State;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PurchaseRequestTest {

    private final Coupon coupon = new Coupon("code", BigDecimal.TEN, LocalDate.now().plusDays(1));

    private final Country country = new Country("country");

    private final State state = new State("state", this.country);

    private final Category category = new Category("category");

    private final Author author = new Author("name", "email", "description");

    private final Book book = new Book("tite",
            "synopsis", "summary", BigDecimal.valueOf(10L),
            10, "ABC",
            LocalDate.now(), this.category, this.author);

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

    private final EntityManager manager = Mockito.mock(EntityManager.class);
    private final CouponRepository couponRepository = Mockito.mock(CouponRepository.class);

    @Test
    @DisplayName("should create a order with state and coupon")
    void toModel1() {
        // given
        this.request.setCouponCode("code");
        this.request.setStateId(1L);

        // when
        when(this.manager.find(Book.class, 1L)).thenReturn(this.book);
        when(this.manager.find(State.class, 1L)).thenReturn(this.state);
        when(this.manager.find(Country.class, 1L)).thenReturn(this.country);
        when(this.couponRepository.getByCode("code")).thenReturn(this.coupon);

        // then
        Purchase purchase = this.request.toModel(this.manager, this.couponRepository);

        assertNotNull(purchase);
        verify(this.manager).find(State.class, 1L);
        verify(this.couponRepository).getByCode("code");
    }

    @Test
    @DisplayName("should create a order without state and with coupon")
    void toModel2() {
        // given
        this.request.setCouponCode("code");

        // when
        when(this.manager.find(Book.class, 1L)).thenReturn(this.book);
        when(this.manager.find(Country.class, 1L)).thenReturn(this.country);
        when(this.couponRepository.getByCode("code")).thenReturn(this.coupon);

        // then
        Purchase purchase = this.request.toModel(this.manager, this.couponRepository);

        assertNotNull(purchase);
        verify(this.manager, never()).find(eq(State.class), anyLong());
        verify(this.couponRepository).getByCode("code");
    }

    @Test
    @DisplayName("should create a order without state and without coupon")
    void toModel3() {
        // given
        this.request.setCouponCode(null);

        // when
        when(this.manager.find(Book.class, 1L)).thenReturn(this.book);
        when(this.manager.find(Country.class, 1L)).thenReturn(this.country);

        // then
        Purchase purchase = this.request.toModel(this.manager, this.couponRepository);

        assertNotNull(purchase);
        verify(this.manager, never()).find(eq(State.class), anyLong());
        verify(this.couponRepository, never()).getByCode(anyString());
    }

}