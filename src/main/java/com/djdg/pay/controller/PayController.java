package com.djdg.pay.controller;

import com.djdg.pay.common.Result;
import com.djdg.pay.model.dto.OrderDTO;
import com.djdg.pay.service.PayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("initOrder")
    public Result createPrepayOrder(OrderDTO orderDTO){
        return payService.createPrepayOrder(orderDTO);
    }







}
