package com.cdc.purchase.entities;

import com.cdc.commons.validations.Document;
import com.cdc.countries.entities.Country;
import com.cdc.coupons.entities.Coupon;
import com.cdc.states.entities.State;
import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.function.Function;

@Entity
@Getter
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    @NotBlank
    @Email
    private String email;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "last_name", nullable = false)
    @NotBlank
    private String lastName;

    @Column(name = "document", nullable = false)
    @NotBlank
    @Document
    private String document;

    @Column(name = "address", nullable = false)
    @NotBlank
    private String address;

    @Column(name = "complement", nullable = false)
    @NotBlank
    private String complement;

    @Column(name = "city", nullable = false)
    @NotBlank
    private String city;

    @ManyToOne
    @NotNull
    private Country country;

    @ManyToOne
    private State state;

    @Column(name = "phone", nullable = false)
    @NotBlank
    private String phone;

    @Column(name = "zip_code", nullable = false)
    @NotBlank
    private String zipCode;

    @OneToOne(mappedBy = "purchase", cascade = CascadeType.PERSIST)
    private Order order;

    @Embedded
    private AppliedCoupon appliedCoupon;

    @Deprecated
    public Purchase() {
    }

    public Purchase(@NotBlank @Email String email,
                    @NotBlank String name,
                    @NotBlank String lastName,
                    @NotBlank @Document String document,
                    @NotBlank String address,
                    @NotBlank String complement,
                    @NotBlank String city,
                    @NotNull Country country,
                    @NotBlank String phone,
                    @NotBlank String zipCode,
                    Function<Purchase, Order> createOrderFunction) {
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
        this.order = createOrderFunction.apply(this);
    }

    public void setState(@NotNull @Valid State state) {
        Assert.notNull(this.country, "To associate a state to a country the country must be not null");
        Assert.isTrue(state.belongsToCountry(this.country), "The state does not belong to the country");
        this.state = state;
    }

    public void applyCoupon(@NotNull Coupon coupon) {
        Assert.notNull(coupon, "The coupon must not be null");
        Assert.isTrue(coupon.isValid(), "The coupon is not valid");
        Assert.isNull(this.appliedCoupon, "Is not possible to change a coupon from a PO");
        Assert.isNull(this.id, "Is not possible to apply a coupon on a PO that was already created");
        this.appliedCoupon = new AppliedCoupon(coupon);
    }

    public boolean hasCoupon() {
        return Optional.ofNullable(this.appliedCoupon).isPresent();
    }

    public String getAddressFormatted() {
        boolean hasState = Optional.ofNullable(this.state).isPresent();

        Object[] params = new Object[]{this.address, this.complement, this.city,
                this.zipCode, hasState ? this.state.getName() : this.country.getName(),
                !hasState ? "#" : this.country.getName(), this.phone};

        return MessageFormat.format("{0} {1}, {2} - {3}, {4}, {5}. {6}", params).trim();
    }

}
