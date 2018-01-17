package com.djdg.pay.service;

import com.djdg.pay.common.PayException;
import com.djdg.pay.common.RedisBean;
import com.djdg.pay.common.Result;
import com.djdg.pay.db.entity.Config;
import com.djdg.pay.db.entity.Order;
import com.djdg.pay.db.repo.ConfigRepository;
import com.djdg.pay.db.repo.OrderRepository;
import com.djdg.pay.model.dto.OrderDTO;
import com.djdg.pay.model.dto.OrderVo;
import com.djdg.pay.model.dto.PrepayDTO;
import com.djdg.pay.model.vo.WxPrepayInfo;
import com.djdg.pay.model.vo.WxPrepayVo;
import com.djdg.pay.utils.DateUtil;
import com.djdg.pay.utils.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * Description:微信支付工具
 * PayService:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/8
 * Time: 14:54
 */
@Service
public class PayService {

    private Logger logger = LoggerFactory.getLogger(PayService.class);

    @Resource
    private ConfigRepository configRepository;
    @Resource
    private OrderRepository orderRepository;
    @Resource
    private WechatService wechatService;
    @Resource
    private RedisBean redisBean;


    public Result createPrepayOrder(OrderDTO orderDTO
    ){
        if(orderDTO.getPayMethod()== Order.PayMethodEnum.WXJS.getKey()){
            String openid = orderDTO.getOpenId();
            if(StringUtils.isEmpty(openid)){
                return Result.fail("请微信登录",401);
            }
        }
        Order order = orderRepository.findFirstByBusinessAppIdAndBusinessOrderIdOrderByCreateTimeDesc(orderDTO.getBusinessAppId(), orderDTO.getBusinessOrderId());
        if(order == null){
            order = new Order();
            // 订单号
            order.setOrderNo(generateOrderNo(orderDTO.getBusinessAppId()));
            order.setBusinessAppId(orderDTO.getBusinessAppId());
            order.setBusinessOrderId(orderDTO.getBusinessOrderId());
            order.setOpenId(orderDTO.getOpenId());
            order.setTotalAmount(orderDTO.getTotalAmount());
            order.setPayStatus(Order.OrderPayStatus.UN_PAY.getValue());
            order = orderRepository.saveAndFlush(order);
        }else{
            order.setOpenId(orderDTO.getOpenId());
            order = orderRepository.saveAndFlush(order);
        }
        return Result.success(new OrderVo(order));
    }


    public Result initWxOrder(Long orderId){
        Order order = orderRepository.findOne(orderId);
        if(order == null){
            return Result.fail("获取订单信息失败");
        }

        String businessAppId = order.getBusinessAppId();
        Config config = getConfig(businessAppId);
        if(config==null){
            return Result.fail("获取支付信息出错");
        }
        PrepayDTO prepayDTO = getPrepayDTO(order, config);
        WxPrepayVo wxPrepayVo = wechatService.getOrder(prepayDTO);
        WxPrepayInfo wxPrepayInfo = new WxPrepayInfo(wxPrepayVo);
        wxPrepayInfo.sign(config.getApiKey());
        return Result.success(wxPrepayInfo);
    }

    public void checkOrder(Long orderId, String businessOrderId, String businessAppId) {
        Order order = orderRepository.findOne(orderId);
        if(order == null) {
            throw new PayException("订单不存在");
        }
        if(!order.getBusinessAppId().equals(businessAppId)
                || !order.getBusinessOrderId().equals(businessOrderId)) {
            logger.error("预支付订单号错误: businessAppId或businessOrderId错误");
            throw new PayException("预支付订单号错误");
        }
        if(order.getPayStatus() != Order.OrderPayStatus.UN_PAY.getValue()) {
            throw new PayException("非待支付订单");
        }
    }

    private String generateOrderNo(String businessAppId) {
        String orderNo = DateUtil.formatDate(new Date(), DateUtil.FormatType.NON_SEPARATOR_SECOND).concat(redisBean.getValueOps().increment(RedisKeyUtil.getOrderSNKey(), 1L).toString());
        String businessName = businessAppId.substring(5);
        businessName = businessName.substring(0, businessName.length()-5);
        return businessName + orderNo;
    }


    private PrepayDTO getPrepayDTO(Order order, Config config) {
        PrepayDTO prepayDTO = new PrepayDTO();
        String openId = order.getOpenId();
        boolean isApp = StringUtils.isEmpty(openId);
        prepayDTO.setBody(config.getBody());
        Boolean enableCreditCart = config.getEnableCreditCart();
        if (!enableCreditCart) prepayDTO.setLimit_pay("no_credit");
        if(!isApp){
            prepayDTO.setAppid(config.getAppId());
            prepayDTO.setMch_id(config.getMchId());
            prepayDTO.setOpenid(openId);
        }else {
            prepayDTO.setAppid(config.getClientConfig().getAppId());
            prepayDTO.setMch_id(config.getClientConfig().getMchId());

        }
        prepayDTO.setNotify_url(config.getNotifyUrl());
        prepayDTO.setOut_trade_no(order.getOrderNo());
        int amount = order.getTotalAmount().multiply(new BigDecimal("100")).intValue();
        prepayDTO.setTotal_fee(String.valueOf(amount));
        prepayDTO.setTrade_type(isApp?"APP":"JSAPI");
        if(!isApp){
            prepayDTO.sign(config.getApiKey());
        }else{
            prepayDTO.sign(config.getClientConfig().getApiKey());
        }

        return prepayDTO;
    }


    public Config getConfig(String appId){

        Config config = configRepository.findByBusinessAppId(appId);
        return config;
    }




}
