package com.djdg.pay.controller;

import com.djdg.pay.common.PayException;
import com.djdg.pay.common.PrintReqResLog;
import com.djdg.pay.db.entity.Order;
import com.djdg.pay.service.PayService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * HtmlController:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/10
 * Time: 14:57
 */
@Controller
public class HtmlController {

    @Resource
    private PayService payService;

    @GetMapping("/toPay")
    @PrintReqResLog()
    public String toPay(@RequestParam(name = "prepayId") Long orderId,
                        @RequestParam(name = "businessAppId") String businessAppId,
                        @RequestParam(name = "businessOrderId") String businessOrderId) {
        payService.checkOrder(orderId, businessOrderId, businessAppId);
        return "index";
    }

}
