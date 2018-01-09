package com.djdg.pay.model.dto;

import lombok.Data;

import javax.xml.bind.annotation.*;
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

    private String sign;

    private String openid;

    private String out_trade_no;

    private String total_fee;

    private String notify_url;

    private String spbill_create_ip = "127.0.0.1";

    private String trade_type;




    public static String createNonceStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }





}
