package com.djdg.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.djdg.pay.common.Result;
import com.djdg.pay.model.dto.PrepayDTO;
import com.djdg.pay.model.vo.WxPrepayInfo;
import com.djdg.pay.model.vo.WxPrepayVo;
import com.djdg.pay.utils.JaxbUtil;
import org.jboss.logging.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.Charset;

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


    Logger logger = Logger.getLogger(WechatService.class);

    @Resource
    private RestTemplate  restTemplate;

    public WxPrepayVo getOrder(PrepayDTO prepayDTO){
        String param = JaxbUtil.convertToXml(prepayDTO, "UTF-8");
        logger.debugv("param="+param);
        HttpEntity<String> stringHttpEntity = getStringHttpEntity(param);
        ResponseEntity<String> responseEntity = restTemplate.exchange(unifiedorderUrl, HttpMethod.POST, stringHttpEntity, String.class);
        String body = responseEntity.getBody();
        String result = new String(body.getBytes(Charset.forName("iso8859-1")));
        logger.debugv("result="+result);
        WxPrepayVo wxPrepayInfo = JaxbUtil.converyToJavaBean(result, WxPrepayVo.class);

        return wxPrepayInfo;
    }



    private static HttpEntity<String> getStringHttpEntity(String param) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/xml; charset=UTF-8");
        headers.setContentType(type);
        return new HttpEntity<String>(param, headers);
    }
    
}
