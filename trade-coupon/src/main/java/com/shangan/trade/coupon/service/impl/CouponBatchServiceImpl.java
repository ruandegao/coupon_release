package com.shangan.trade.coupon.service.impl;

import com.shangan.trade.coupon.db.dao.CouponBatchDao;
import com.shangan.trade.coupon.db.model.CouponBatch;
import com.shangan.trade.coupon.service.CouponBatchService;
import com.shangan.trade.coupon.utils.RedisWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponBatchServiceImpl implements CouponBatchService {

    @Autowired
    private CouponBatchDao couponBatchDao;

    @Autowired
    private RedisWorker redisWorker;


    @Override
    public boolean insertCouponBatch(CouponBatch couponBatch) {
        return couponBatchDao.insertCouponBatch(couponBatch);
    }

    @Override
    public List<CouponBatch> queryCouponBatchList() {
        return couponBatchDao.queryCouponBatchList();
    }

    @Override
    public boolean pushBatchListRuleToCache() {
        List<CouponBatch> couponBatches = couponBatchDao.queryCouponBatchList();
        for (CouponBatch couponBatch : couponBatches) {
            redisWorker.setKey("couponBatchRule:" + couponBatch.getId(), couponBatch.getRule());
        }
        return true;
    }

    @Override
    public CouponBatch queryCouponBatchById(long id) {
        return couponBatchDao.queryCouponBatchById(id);
    }
}
