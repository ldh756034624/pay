package com.djdg.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.djdg.pay.common.Result;
import com.djdg.pay.model.dto.OrderDTO;
import com.djdg.pay.model.dto.RefundDTO;
import com.djdg.pay.service.PayService;
import org.omg.CORBA.ORB;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Result createPrepayOrder(@Validated @RequestBody OrderDTO orderDTO) {
        return payService.createPrepayOrder(orderDTO);
    }


    @GetMapping("getPrepay")
    public Result getPrepay(@RequestParam Long payOrderId) {
        return payService.initWxOrder(payOrderId);
    }


    /**
     * description: 退款
     */
    @PostMapping("/order/refund")
    public Result refundOrder(@RequestBody RefundDTO refundDTO) {
        return payService.refundOrder(refundDTO);
    }

    /**
     * 根据微信流水查询
     *
     * @param no
     * @return
     */
    @GetMapping("/order/info")
    public Result getOrderInfoByNo(@RequestParam String no) {
        return payService.getOrderInfoByNo(no);
    }

    @GetMapping("/order/info/batch")
    public Result batchQueryByPayInfId(@RequestParam String ids, @RequestParam String bid) {
        return payService.batchQueryByPayInfId(ids, bid);
    }

    @GetMapping("/order/payinfo")
    public Result orderPayInfo(@RequestParam String id,@RequestParam String bid) {
        return payService.orderPayInfo(id,bid);
    }

}
