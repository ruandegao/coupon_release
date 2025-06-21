package com.shangan.trade.coupon.db.mappers;

import com.shangan.trade.coupon.db.model.CouponCode;

public interface CouponCodeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponCode record);

    int insertSelective(CouponCode record);

    CouponCode selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponCode record);

    int updateByPrimaryKey(CouponCode record);

    /**
     * 通过券码查询对应的券码信息
     *
     * @param code
     * @return
     */
    CouponCode queryCouponByCode(String code);
}