package com.djdg.pay.service;

import com.djdg.pay.common.Result;
import com.djdg.pay.model.dto.PrepayDTO;
import com.djdg.pay.model.vo.WxPrepayInfo;
import com.djdg.pay.utils.JaxbUtil;
import org.springframework.http.*;
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

    public WxPrepayInfo getOrder(PrepayDTO prepayDTO){
        String param = JaxbUtil.convertToXml(prepayDTO, "UTF-8");
        HttpEntity<String> stringHttpEntity = getStringHttpEntity(param);
        ResponseEntity<String> responseEntity = restTemplate.exchange(unifiedorderUrl, HttpMethod.POST, stringHttpEntity, String.class);
        String body = responseEntity.getBody();
        WxPrepayInfo wxPrepayInfo = JaxbUtil.converyToJavaBean(body, WxPrepayInfo.class);
        return wxPrepayInfo;
    }


    private static HttpEntity<String> getStringHttpEntity(String param) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/xml; charset=UTF-8");
        headers.setContentType(type);
        return new HttpEntity<String>(param, headers);
    }
    
}
