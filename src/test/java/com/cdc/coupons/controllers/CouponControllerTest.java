package com.cdc.coupons.controllers;

import com.cdc.BaseRestControllerTest;
import com.cdc.RestControllerTest;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.BigRange;
import net.jqwik.api.constraints.StringLength;
import net.jqwik.time.api.Dates;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@RestControllerTest
class CouponControllerTest extends BaseRestControllerTest {

    private final Set<String> codes = new HashSet<>();

    @Property(tries = 10)
    @Label("should create a coupon")
    void create(@ForAll @AlphaChars @StringLength(min = 1, max = 255) String code,
                @ForAll @BigRange(min = "1", max = "90") BigDecimal discount,
                @ForAll("futureDates") LocalDate expirationDate) throws Exception {
        assumeTrue(this.codes.add(code));

        // given
        Map<String, Object> content = Map.of("code", code,
                "discount", discount,
                "expirationDate", expirationDate.format(DateTimeFormatter.ISO_DATE));

        // then
        super.post("/coupons", content).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        super.post("/coupons", content).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Provide
    Arbitrary<LocalDate> futureDates() {
        return Dates.dates().atTheEarliest(LocalDate.now().plusDays(1));
    }
}