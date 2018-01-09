package com.djdg.pay.model.vo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * WxPrepayVo:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/9
 * Time: 18:38
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class WxPrepayVo {
    @XmlElement(name = "return_code")
    private String returnCode;
    @XmlElement(name="return_msg")
    private String returnMsg;
    @XmlElement(name="appid")
    private String appId;
    @XmlElement(name="mch_id")
    private String mchId;
    @XmlElement(name="nonce_str")
    private String nonceStr;
    @XmlElement(name="sign")
    private String sign;
    @XmlElement(name="result_code")
    private String resultCode;
    @XmlElement(name="prepay_id")
    private String prepayId;
    @XmlElement(name="trade_type")
    private String tradeType;


}
