package com.djdg.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.djdg.pay.common.RedisKeyUtil;
import com.djdg.pay.common.Result;
import com.djdg.pay.db.entity.Order;
import com.djdg.pay.db.repo.OrderRepository;
import com.djdg.pay.model.vo.PayNotifyVO;
import com.djdg.pay.utils.HttpUtilClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.djdg.pay.common.Result.fail;
import static com.djdg.pay.common.Result.success;

/**
 * @Description:
 * @Auther Demon
 * @Date 2017/12/8 14:29 星期五
 */
@Service
public class CallbackService {

    Logger logger = LoggerFactory.getLogger(CallbackService.class);

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 15s、15s、30s、3min、30min、30min、30min、30min、1h
     */
    private static int[] delayRates = { 0, 15, 15, 30, 180, 1800, 1800, 1800, 1800, 3600 };
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public Result<?> callback(String orderCallBackUrl, PayNotifyVO payNotifyVO) {

        String json = HttpUtilClient.httpPost(orderCallBackUrl, payNotifyVO.toString());

        logger.debug("processOrders order {}, result is {} ", orderCallBackUrl, json);

        // 回调失败则开始重试机制
        if (!isCallbackSuccess(json)) {
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            String timesKey = RedisKeyUtil.getOrderNotifyTimes(payNotifyVO.getApp_id(), payNotifyVO.getOrder_id());
            // 记录第几次重试
            Long times = redisTemplate.opsForValue().increment(timesKey, 1);
            // 总的重试时间持续184min，这里设置过期时间过190min
            redisTemplate.expire(timesKey, 190, TimeUnit.MINUTES);
            logger.info("回调重试：尝试进行第{}次重试，delay={}s, timesKey={}, notifyParams={}", times, delayRates[times.intValue()], timesKey, payNotifyVO.toString());
            retryCallback(executor, delayRates[times.intValue()], orderCallBackUrl, payNotifyVO, timesKey);
            return fail("回调失败，尝试重试中...");
        } else {
            updateOrderStatus(payNotifyVO.getNotify_id());
            return success("回调成功");
        }
    }

    private void retryCallback(ScheduledExecutorService executor, int delay, String url, PayNotifyVO payNotifyVO, String timesKey) {

        TimerTask doCallback = new TimerTask() {

            @Override
            public void run() {
                try {
                    String result = HttpUtilClient.httpPost(url, payNotifyVO.toString());
                    Long times = redisTemplate.opsForValue().increment(timesKey, 1);
                    if (!isCallbackSuccess(result)) {
                        // delayRates.length比实际次数多1
                        if (times < delayRates.length) {
                            logger.info("回调重试：尝试进行第{}次重试，delay={}s, timesKey={}, notifyParams={3}", times, delayRates[times.intValue()], timesKey, payNotifyVO.toString());
                            retryCallback(executor, delayRates[times.intValue()], url, payNotifyVO, timesKey);
                        } else {
                            logger.info("回调重试：完成指定回调次数之后仍未成功，该订单不再通知！已完成重试次数={}, timesKey={}, result={}", times - 1, timesKey, result);
                            executor.shutdownNow();
                        }
                    } else {
                        logger.info("回调重试：回调成功！已完成重试次数={}, timesKey={}, result={}", times - 1, timesKey, result);
                        redisTemplate.delete(timesKey);
                        // 更新支付记录状态
                        updateOrderStatus(payNotifyVO.getNotify_id());
                        executor.shutdownNow();
                    }
                } catch (Exception e) {
                    logger.error("回调重试：支付回调重试异常timesKey=" + timesKey, e);
                    executor.shutdownNow();
                }
            }

        };

        executor.schedule(doCallback, delay, TimeUnit.SECONDS);
    }

    /**
     * 根据回调接口返回的信息判断是否回调成功
     *
     * @param result
     * @return
     */
    private boolean isCallbackSuccess(String result) {
        if (StringUtils.isBlank(result)) {
            return false;
        }
        JSONObject jsonResult = JSONObject.parseObject(result);
        if (jsonResult.getInteger("statusCode") == null || jsonResult.getInteger("statusCode") != 0) {
            return false;
        }
        return true;
    }

    /**
     * 更新支付记录为已回调
     *
     * @param id
     */
    private void updateOrderStatus(Long id) {
        try {
            Order order = orderRepository.findOne(id);
            order.setStatus(1);
            orderRepository.save(order);
            logger.info("支付回调：回调成功更新支付记录状态成功");
        } catch (Exception e) {
            logger.error("支付回调：回调成功更新支付记录状态异常", e);
        }
    }

}
