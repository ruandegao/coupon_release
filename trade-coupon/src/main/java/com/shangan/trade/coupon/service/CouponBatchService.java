package com.shangan.trade.coupon.service;

import com.shangan.trade.coupon.db.model.CouponBatch;

import java.util.List;

public interface CouponBatchService {

    /**
     * 新增一个券批次
     *
     * @param couponBatch
     * @return
     */
    boolean insertCouponBatch(CouponBatch couponBatch);


    /**
     * 查询优惠券批次列表
     * @return
     */
    List<CouponBatch> queryCouponBatchList();

    /**
     * 将批次信息的规则信息push到Redis中
     * @return
     */
    boolean pushBatchListRuleToCache();


    /**
     * 根据ID查询对应的券批次
     *
     * @param id
     * @return
     */
    CouponBatch queryCouponBatchById(long id);

}
