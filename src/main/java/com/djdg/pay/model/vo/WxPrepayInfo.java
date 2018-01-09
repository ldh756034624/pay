package com.djdg.pay.model.vo;

import lombok.Data;

/**
 * @Description: 预支付订单信息
 * @Auther Demon
 * @Date 2017/11/16 16:54 星期四
 */
@Data
public class WxPrepayInfo  {

    private String appId;
    /** 支付签名随机串，不长于 32 位 */
    private String nonceStr;
    /** 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***） */
    private String packageParam;
    /**  签名方式:MD5 */
    private String signType;
    /** 支付签名 */
    private String paySign;
    private String timestamp;
    private String prepayId;
    private String partnerId;
    private String mchId;
    private String errCode;
    private String errCodeDes;
    private String resultCode;




}
