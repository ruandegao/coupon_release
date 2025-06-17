package com.shangan.trade.coupon;

import com.shangan.trade.coupon.utils.RedisLock;
import com.shangan.trade.coupon.utils.RedisWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    public RedisWorker redisWorker;

    @Autowired
    public RedisLock redisLock;

//    @Autowired
//    public CouponSendService couponSendService;

    @Test
    public void redisSetKVTest(){
        redisWorker.setKey("name","Tom");
    }

    @Test
    public void redisGet(){
        System.out.println(redisWorker.getValue("name"));
    }


    @Test
    public void redisLockTest() {
        String request1Id = UUID.randomUUID().toString();
        boolean result1 = redisLock.tryGetLock("redisLock3", request1Id, 500);
        System.out.println("  boolean result1 =" + result1);
         redisLock.releaseLock("redisLock3",request1Id);

        boolean result2 = redisLock.tryGetLock("redisLock3", UUID.randomUUID().toString(), 1000 * 2);
        System.out.println("  boolean result2 =" + result2);
    }


//    @Test
//    public void sendUserCouponSynWithLock(){
//        boolean res = couponSendService.sendUserCouponSynWithLock(10, 86869);
//        System.out.println(res);
//    }

}
