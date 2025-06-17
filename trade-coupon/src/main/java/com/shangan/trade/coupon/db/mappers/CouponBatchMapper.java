package com.shangan.trade.coupon.db.mappers;

import com.shangan.trade.coupon.db.model.CouponBatch;

import java.util.List;

public interface CouponBatchMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponBatch record);

    int insertSelective(CouponBatch record);

    CouponBatch selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponBatch record);

    int updateByPrimaryKey(CouponBatch record);

    List<CouponBatch> queryCouponBatchList();

    /**
     * 更新发券后券批次数量记录信息
     * @param id
     * @return
     */
    int updateSendCouponBatchCount(Long id);
}