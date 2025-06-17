package com.shangan.trade.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.coupon.db.dao.CouponBatchDao;
import com.shangan.trade.coupon.db.dao.CouponDao;
import com.shangan.trade.coupon.db.mappers.CouponBatchMapper;
import com.shangan.trade.coupon.db.model.Coupon;
import com.shangan.trade.coupon.db.model.CouponBatch;
import com.shangan.trade.coupon.db.model.CouponRule;
import com.shangan.trade.coupon.exceptions.BizException;
import com.shangan.trade.coupon.service.CouponSendService;
import com.shangan.trade.coupon.utils.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class CouponSendServiceImpl implements CouponSendService {

    @Autowired
    private CouponBatchMapper couponBatchMapper;

    @Autowired
    private CouponBatchDao couponBatchDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private RedisLock redisLock;

//    @Autowired
//    private TaskDao taskDao;
//
//    @Autowired
//    private MessageSender messageSender;
//
//    @Autowired
//    private CouponRemindService couponRemindService;

    /**
     * 在发送优惠券之前尝试获取锁
     * @param batchId
     * @param userId
     * @return
     */
    @Override
    public boolean sendUserCouponSynWithLock(long batchId, long userId) {
        //设置锁的名称(相同的业务用同一个就行了)
        String lockKeyName = "sendCoupon";

        //设置每一个request的requestID，用uuid来实现
        String requestId = UUID.randomUUID().toString();
        try {
            if (redisLock.tryGetLock(lockKeyName, requestId, 200)) {
                //获取成功锁后，执行业务逻辑
                return sendUserCouponSyn(batchId, userId);
            }
        } catch (Exception e) {
            log.error("sendUserCouponSynWithLock error batchId={} userId={}", batchId, userId, e);
            throw new BizException(e.getMessage());
        } finally {
            //最后一定要释放锁
            redisLock.releaseLock(lockKeyName,requestId);  //同个请求，加锁和解锁都是同一个requestId
        }
        return false;
    }

        /**
         * 同步发券
         * @param batchId
         * @param userId
         * @return
         */
        @Transactional(rollbackFor = Exception.class)
        @Override
        public boolean sendUserCouponSyn(long batchId, long userId) {
            //1.查询批次信息
            CouponBatch couponBatch = couponBatchMapper.selectByPrimaryKey(batchId);
            //2.判断该batchId，对应的券批次信息是否存储在
            if (couponBatch == null) {
                log.error("sendUserCouponSyn error couponBatch not exits batchId:{} userId:{}", batchId, userId);
                throw new BizException("批次信息不存在");
            }
            //3.判断该批次的券余量是否大于0
            if (couponBatch.getAvailableCount() <= 0) {
                log.error("sendUserCouponSyn error  availableCount is not enough batchId:{} userId:{}", batchId, userId);
                throw new BizException("该批次可发放数量不足");
            }
            //4.更新券余量和券发送数量
            log.info("updateSendCouponBatchCount batchId={} userId={}", batchId, userId);
            boolean updateSendCouponBatchRes = couponBatchDao.updateSendCouponBatchCount(batchId);
            if (!updateSendCouponBatchRes) {
                log.error("couponBatch updateSendCouponBatchRes error batchId={} userId={}", batchId, userId);
                throw new BizException("更新券券批次数量失败");
            }

            //5.优惠券表中新增该用户优惠券记录
            Coupon coupon = createCoupon(couponBatch, userId);
            boolean insertCouponRes = couponDao.insertCoupon(coupon);
            if (!insertCouponRes) {
                log.error("couponBatch insertCoupon error batchId={} userId={}", batchId, userId);
                throw new BizException("用户发券失败");
            }
            //6.插入一个券过期提醒任务
            //将JSON中的rule规则，转化成CouponRule对象
//            CouponRule couponRule = JSON.parseObject(couponBatch.getRule(), CouponRule.class);
//            couponRemindService.insertCouponRemindTask(userId,coupon.getId(),couponRule);
            return true;
        }

        public Coupon createCoupon(CouponBatch couponBatch, long userId) {
            Coupon coupon = new Coupon();
            coupon.setUserId(userId);
            coupon.setBatchId(couponBatch.getId());
            //收到的时间用当前系统的时间
            coupon.setReceivedTime(new Date());
            String rule = couponBatch.getRule();
            if (StringUtils.isEmpty(rule)) {
                log.error("couponBatch rule is empty batchId={} userId={}", couponBatch.getId(), userId);
                throw new BizException("券批次规则为空");
            }
            /*
             * 将JSON中的rule规则，转化成CouponRule对象
             */
            CouponRule couponRule = JSON.parseObject(rule, CouponRule.class);
            //设置到期时间
            coupon.setValidateTime(couponRule.getEndTime());
            coupon.setCouponName(couponBatch.getCouponName());
            //默认为0 未使用
            coupon.setStatus(0);
            coupon.setCreateTime(new Date());
            return coupon;
        }

//    @Override
//    public boolean sendUserCouponBatch(long batchId, Set<Long> userIdSet) {
//        for (Long userId : userIdSet) {
//            //插入任务表
//            Task task = new Task();
//            task.setStatus(0);
//            task.setTryCount(0);
//            task.setBizType("send_coupon");
//            //UUID
//            task.setBizId(UUID.randomUUID().toString());
//
//            SendCouponTaskModel sendCouponTaskModel = new SendCouponTaskModel();
//            sendCouponTaskModel.setBatchId(batchId);
//            sendCouponTaskModel.setUserId(userId);
//            task.setBizParam(JSON.toJSONString(sendCouponTaskModel));
//
//            task.setModifiedTime(new Date());
//            task.setCreateTime(new Date());
//
//            boolean res = taskDao.insertTask(task);
//            if (res) {
//                //插入任务记录，成功再发送消息
//                messageSender.sendMessage("send-batch-coupon", JSON.toJSONString(task));
//            } else {
//                log.error("insert task table error userId:{} batchId:{}", userId, batchId);
//            }
//        }
//        return false;
//    }
}
