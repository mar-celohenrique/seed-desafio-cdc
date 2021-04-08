package com.cdc.coupons.repositories;

import com.cdc.coupons.entities.Coupon;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends org.springframework.data.repository.Repository<Coupon, Long> {

    Coupon getByCode(String code);

}
