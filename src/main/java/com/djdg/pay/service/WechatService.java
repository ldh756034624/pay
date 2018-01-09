package com.djdg.pay.service;

import com.djdg.pay.common.Result;
import com.djdg.pay.model.vo.WxPrepayInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * WechatService:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/8
 * Time: 17:25
 */
@Component
public class WechatService {

    public static String unifiedorderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    
    @Resource
    private RestTemplate  restTemplate;

    public WxPrepayInfo getOrder(){



        return new WxPrepayInfo();
    }
    
    
}
