package com.djdg.pay.model.dto;

import com.djdg.pay.utils.MD5Util;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.*;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * PrepayDTO:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/8
 * Time: 17:44
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class PrepayDTO {

    private String appid;

    private String body;

    private String limit_pay;

    private String mch_id;

    private String nonce_str = createNonceStr();

    private String notify_url;

    private String openid;

    private String out_trade_no;

    private String total_fee;

    private String trade_type;

    private String sign;

    private String spbill_create_ip = "127.0.0.1";



    public String sign(String key){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("appid=").append(getAppid());
        stringBuffer.append("body=").append(getBody());
        if(StringUtils.isNotEmpty(limit_pay)){
            stringBuffer.append("limit_pay=").append(getLimit_pay());
        }
        stringBuffer.append("mch_id=").append(getMch_id());
        stringBuffer.append("nonce_str=").append(getNonce_str());
        stringBuffer.append("notify_url=").append(getNotify_url());
        if(StringUtils.isNotEmpty(openid)){
            stringBuffer.append("openid=").append(getOpenid());
        }
        stringBuffer.append("out_trade_no=").append(getOut_trade_no());
        stringBuffer.append("spbill_create_ip=").append(getSpbill_create_ip());
        stringBuffer.append("total_fee=").append(getTotal_fee());
        stringBuffer.append("trade_type=").append(getTrade_type());
        stringBuffer.append("&key=").append(key);
        this.sign = MD5Util.getMD5(sign.toString()).toUpperCase();
        return sign;
    }




    public static String createNonceStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }





}
