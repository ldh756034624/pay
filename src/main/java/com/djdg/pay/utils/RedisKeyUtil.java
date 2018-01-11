package com.djdg.pay.utils;

/**
 * @Description: redis key
 * @Auther Demon
 * @Date 2017/11/16 14:28 星期四
 */
public class RedisKeyUtil {

    /**
     * 订单回调次数Key h9:pay:order:notify:times:{appId}:{orderId}
     */
    public static String getOrderNotifyTimes(String businessAppId, String orderId) {
        return "common:pay:order:notify:times:" + businessAppId + ":" + orderId;
    }

    /** 订单自增序列号 */
    public static String getOrderSNKey() {
        return "h9:pay:order:sn";
    }
}
