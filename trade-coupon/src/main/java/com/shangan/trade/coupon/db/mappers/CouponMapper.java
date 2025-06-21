package com.shangan.trade.coupon.db.mappers;

import com.shangan.trade.coupon.db.model.Coupon;

import java.util.List;

public interface CouponMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Coupon record);

    int insertSelective(Coupon record);

    Coupon selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Coupon record);

    int updateByPrimaryKey(Coupon record);

    List<Coupon> queryUserCoupons(Long userId);
}