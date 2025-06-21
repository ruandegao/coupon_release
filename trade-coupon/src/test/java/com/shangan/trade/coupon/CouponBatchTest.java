package com.shangan.trade.coupon;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.coupon.db.dao.CouponBatchDao;
import com.shangan.trade.coupon.db.model.CouponBatch;
import com.shangan.trade.coupon.service.CouponBatchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CouponBatchTest {

    @Autowired
    private CouponBatchDao couponBatchDao;

    @Autowired
    private CouponBatchService couponBatchService;

    @Test
    public void insertCouponBatch() {
        CouponBatch couponBatch = new CouponBatch();
        couponBatch.setBatchName("圣诞节优惠券批次");
        couponBatch.setCouponName("满200减20优惠券");
        couponBatch.setCouponType(1);
        couponBatch.setRule("rule");
        couponBatch.setTotalCount(100L);
        couponBatch.setAvailableCount(100L);
        couponBatch.setUsedCount(20L);
        couponBatch.setAssignCount(10L);
        couponBatch.setGrantType(1);
        couponBatch.setCreateTime(new Date());
        couponBatch.setStatus(1);
        couponBatchDao.insertCouponBatch(couponBatch);
    }

    @Test
    public void deleteCouponBatchTest() {
        boolean deleteResult = couponBatchDao.deleteCouponBatchById(9);
        System.out.println(deleteResult);
    }

    @Test
    public void queryCouponBatchTest() {
        CouponBatch couponBatch = couponBatchDao.queryCouponBatchById(2);
        System.out.println(JSON.toJSONString(couponBatch));
    }

    @Test
    public void updateCouponBatch() {
        CouponBatch couponBatch = couponBatchDao.queryCouponBatchById(9);
        couponBatch.setBatchName(couponBatch.getBatchName() + " update");
        couponBatchDao.updateCouponBatch(couponBatch);
    }

    @Test
    public void pushBatchListRuleToCacheTest() {
        boolean res = couponBatchService.pushBatchListRuleToCache();
        System.out.println(res);
    }
}
