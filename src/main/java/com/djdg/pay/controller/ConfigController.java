package com.djdg.pay.controller;

import com.djdg.pay.common.Result;
import com.djdg.pay.db.entity.Config;
import com.djdg.pay.model.dto.ConfigDto;
import com.djdg.pay.service.ConfigService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * ConfigController:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/5
 * Time: 16:06
 */
@RestController
@RequestMapping("/config")
public class ConfigController {
    
    @Resource
    private ConfigService configService;
    
    @GetMapping("/regist")
    public Result regist(ConfigDto configDto){
        Result result = configService.register(configDto);
        return result;
    }
}