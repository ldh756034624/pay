package com.djdg.pay.service;

import com.djdg.pay.common.Result;
import com.djdg.pay.db.entity.Config;
import com.djdg.pay.db.entity.Order;
import com.djdg.pay.db.repo.ConfigRepository;
import com.djdg.pay.db.repo.OrderRepository;
import com.djdg.pay.model.dto.OrderDTO;
import com.djdg.pay.model.dto.OrderVo;
import com.djdg.pay.model.dto.PrepayDTO;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * PayService:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/8
 * Time: 14:54
 */
@Service
public class PayService {

    @Resource
    private ConfigRepository configRepository;
    @Resource
    private OrderRepository orderRepository;
    
    
    @Resource
    private RestTemplate restTemplate;

    public Result createPrepayOrder(OrderDTO orderDTO
    ){
        if(!orderDTO.isApp()){
            String openid = orderDTO.getOpenId();
            if(StringUtils.isEmpty(openid)){
                return Result.fail("请微信登录",401);
            }

        }
        Order order = orderRepository.findByOrderNo(orderDTO.getOrderNo());
        if(order == null){
            order = new Order();
            order.setBusinessAppId(orderDTO.getBusinessAppId());
            order.setBusinessOrderId(orderDTO.getBusinessOrderId());
            order.setOrderNo(orderDTO.getOrderNo());
            order.setOpenId(orderDTO.getOpenId());
            order.setTotalAmount(orderDTO.getTotalAmount());
            order.setPayStatus(Order.OrderPayStatus.UN_PAY.getValue());
            order = orderRepository.save(order);
        }else{
            order.setOpenId(orderDTO.getOpenId());
            order = orderRepository.save(order);
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
        PrepayDTO prepayDTO = new PrepayDTO();
        prepayDTO.setAppid(config.getAppId());
        prepayDTO.setBody(config.getBody());
        Boolean enableCreditCart = config.getEnableCreditCart();
        if (!enableCreditCart) prepayDTO.setLimit_pay("no_credit");
        prepayDTO.setMch_id(config.getMchId());
        prepayDTO.setNotify_url(config.getNotifyUrl());
        String openId = order.getOpenId();
        if(!StringUtils.isEmpty(openId)){
            prepayDTO.setOpenid(openId);
        }
        prepayDTO.setNotify_url(config.getNotifyUrl());


        //

        return Result.success();
    }



    public Config getConfig(String appId){
        if(configs.containsKey(appId)){
            return configs.get(configs);
        }
        Config config = configRepository.findByBusinessAppId(appId);
        configs.put(appId, config);
        return config;
    }

    static Map<String, Config> configs = new ConcurrentHashMap<>();


    public static void clearConfig(String key){
        configs.remove(key);
    }




}
