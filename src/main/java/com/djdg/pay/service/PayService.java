package com.djdg.pay.service;

import com.djdg.pay.common.Result;
import com.djdg.pay.db.entity.Order;
import com.djdg.pay.db.repo.ConfigRepository;
import com.djdg.pay.db.repo.OrderRepository;
import com.djdg.pay.model.dto.OrderDTO;
import com.djdg.pay.model.dto.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

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
        String openid = orderDTO.getOpenId();
        if(StringUtils.isEmpty(openid)){
            return Result.fail("请微信登录",401);
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
           //TODO 考虑金额变更
        }
        return Result.success(new OrderVo(order));
    }


    public Result initWxOrder(){

        //

        return Result.success();
    }








}
