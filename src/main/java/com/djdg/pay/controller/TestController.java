package com.djdg.pay.controller;

import com.djdg.pay.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * TestController:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/5
 * Time: 15:11
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("hello")
    public Result hello(){
        return Result.success();
    }




}
