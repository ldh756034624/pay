package com.djdg.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.djdg.pay.common.Result;
import com.djdg.pay.model.dto.OrderDTO;
import com.djdg.pay.service.PayService;
import org.omg.CORBA.ORB;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * PayController:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/8
 * Time: 11:34
 */
@RestController
@RequestMapping("/")
public class PayController {

    @Resource
    private PayService payService;

    @PostMapping("initOrder")
    public Result createPrepayOrder(@Validated @RequestBody OrderDTO orderDTO){
        return payService.createPrepayOrder(orderDTO);
    }



    @GetMapping("getPrepay")
    public Result getPrepay(@RequestParam  Long orderId){
        return payService.initWxOrder(orderId);
    }





}
