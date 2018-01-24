package com.djdg.pay.service;

import com.alibaba.fastjson.JSON;
import com.djdg.pay.common.PayException;
import com.djdg.pay.common.Result;
import com.djdg.pay.db.entity.Config;
import com.djdg.pay.db.entity.Order;
import com.djdg.pay.db.repo.ConfigRepository;
import com.djdg.pay.db.repo.OrderRepository;
import com.djdg.pay.model.dto.WxNotificationDTO;
import com.djdg.pay.model.vo.PayNotifyVO;
import com.djdg.pay.model.vo.WxPayResponseVO;
import com.djdg.pay.utils.DateUtil;
import com.djdg.pay.utils.WechatUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.SortedMap;

/**
 * @Description: 回调处理
 * @Auther Demon
 * @Date 2018/1/9 15:14 星期二
 */
@Service
public class NotifyService {

    private Logger logger = LoggerFactory.getLogger(NotifyService.class);

    @Autowired
    private ConfigRepository configRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CallbackService callbackService;

    public WxPayResponseVO processWxNotification(WxNotificationDTO notification, String businessAppId) {
        Config paymentConfig = configRepository.findByBusinessAppId(businessAppId);
        SortedMap<String, String> map = notification.getNotify_params();
        String log = JSON.toJSONString(map);
        logger.info("notify params from wx : " + log);
        WxPayResponseVO response = new WxPayResponseVO();
        try {
//            if (!WechatUtil.verifySign(paymentConfig, map)) {
//                logger.info("wxPayResponse: verifySign failed, return false ");
//                response.setReturn_code(WechatUtil.FAILED);
//                return response;
//            }
            if (StringUtils.equals(notification.getReturn_code(), WechatUtil.SUCCESS)) {
                processOrder(notification, log, paymentConfig.getCallbackUrl());
                response.setReturn_code(WechatUtil.SUCCESS);
            } else {
                logger.error("get failed message from wxpay:" + notification.getReturn_msg());
                response.setReturn_code(WechatUtil.FAILED);
            }
        } catch (Exception ex) {
            logger.error("wxPayResponse error:", ex);
            response.setReturn_code(WechatUtil.FAILED);
        }
        return response;

    }

    private void processOrder(WxNotificationDTO notification, String log, String callbackUrl) {
        String orderNo = notification.getOut_trade_no();
        Order order = orderRepository.findByOrderNo(orderNo);
        int payMethod = order.getPayMethod();
        if(order == null) {
            logger.error("回调商户订单号找不到对应订单");
            throw new PayException( "没有对应订单");
        }
        if(order.getStatus() != Order.OrderPayStatus.UN_PAY.getValue()) {
            logger.error("回调商户订单号对应订单非待支付状态");
            throw new PayException("回调商户订单号对应订单非待支付状态");
        }

        BigDecimal totalFee = new BigDecimal(notification.getTotal_fee());
        BigDecimal totalAmount = order.getTotalAmount().multiply(new BigDecimal(100)).setScale(0);
        if(totalAmount.compareTo(totalFee) != 0) {
            logger.error("回调商户订单支付金额异常:total_fee is " + notification.getTotal_fee() + "  but totalAmount is " + order.getTotalAmount());
            throw new PayException("回调商户订单支付金额异常");
        }
        order.setPayStatus(Order.OrderPayStatus.PAID.getValue());
        order.setNotifyLog(log);
        order.setTransactionId(notification.getTransaction_id());
        order = orderRepository.save(order);

        // 回调业务系统
        callbackBusinessSystem(callbackUrl, order, notification, payMethod);

    }

    private void callbackBusinessSystem(String callbackUrl, Order order, WxNotificationDTO notification, int payMethod) {
        PayNotifyVO payNotifyObject = new PayNotifyVO();
        payNotifyObject.setNotify_id(order.getId());
        payNotifyObject.setOrder_id(order.getBusinessOrderId());
        payNotifyObject.setPay_time(DateUtil.formatDate(order.getUpdateTime(), DateUtil.FormatType.NON_SEPARATOR_SECOND));
        payNotifyObject.setPay_way(payMethod);
        if(notification.getCash_fee() != null) {
            payNotifyObject.setCash_fee(new BigDecimal(notification.getCash_fee()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());
        }
        payNotifyObject.setTotal_fee(new BigDecimal(notification.getTotal_fee()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());
        if(notification.getCoupon_fee() != null) {
            payNotifyObject.setCoupon_fee(new BigDecimal(notification.getCoupon_fee()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toString());
        }
        payNotifyObject.setCoupon_count(notification.getCoupon_count());
        payNotifyObject.setApp_id(order.getBusinessAppId());
        payNotifyObject.setPay_type("order");
        payNotifyObject.setTrade_no(notification.getTransaction_id());
        payNotifyObject.setNotify_time(DateUtil.formatDate(new Date(), DateUtil.FormatType.NON_SEPARATOR_SECOND));
        payNotifyObject.setNotify_type("pay_notify");

        Result<?> rtn = callbackService.callback(callbackUrl, payNotifyObject);
        logger.info("call back result:{}", rtn.getMsg());
    }


}
