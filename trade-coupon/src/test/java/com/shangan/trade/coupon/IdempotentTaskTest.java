package com.shangan.trade.coupon;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.coupon.db.dao.IdempotentTaskDao;
import com.shangan.trade.coupon.db.model.IdempotentTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IdempotentTaskTest {

    @Autowired
    private IdempotentTaskDao idempotentTaskDao;

    @Test
    public void insertIdempotentTaskTest() {
        IdempotentTask idempotentTask = new IdempotentTask();
        idempotentTask.setBizType("send_coupon");
        idempotentTask.setBizId("123456");
        idempotentTask.setCreateTime(new Date());
        boolean result = idempotentTaskDao.insertIdempotentTask(idempotentTask);
        System.out.println(result);
    }

    @Test
    public void queryIdempotentTaskTest(){
        IdempotentTask idempotentTask = idempotentTaskDao.queryIdempotentTaskByBiz("send_coupon", "123456");
        System.out.println(JSON.toJSONString(idempotentTask));
    }
}
