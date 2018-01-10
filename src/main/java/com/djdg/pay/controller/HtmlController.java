package com.djdg.pay.controller;

import com.djdg.pay.common.PrintReqResLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * HtmlController:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/10
 * Time: 14:57
 */
@Controller
public class HtmlController {

    @GetMapping("/toPay")
    @PrintReqResLog()
    public String toPay(@RequestParam(required = false) Long orderId,Model model){
        model.addAttribute("orderId", orderId);
        return "index";
    }

}
