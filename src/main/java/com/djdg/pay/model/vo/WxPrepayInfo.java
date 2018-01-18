package com.djdg.pay.model.vo;

import com.djdg.pay.model.dto.PrepayDTO;
import com.djdg.pay.service.PayService;
import com.djdg.pay.utils.MD5Util;
import com.djdg.pay.utils.WechatUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Description: 预支付订单信息
 * @Auther Demon
 * @Date 2017/11/16 16:54 星期四
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class WxPrepayInfo  {

    private String appId;
    /** 支付签名随机串，不长于 32 位 */
    private String nonceStr;
    /** 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***） */
    private String packageParam;
    /**  签名方式:MD5 */
    private String signType="";
    /** 支付签名 */
    private String paySign;

    private long timestamp;

    private String prepayId;

    private String partnerId;

    private String mchId;

    public WxPrepayInfo() {
    }

    public WxPrepayInfo(WxPrepayVo wxPrepayVo) {
        appId = wxPrepayVo.getAppId();
        mchId = wxPrepayVo.getMchId();
        partnerId = wxPrepayVo.getMchId();
        nonceStr = wxPrepayVo.getNonceStr();
        packageParam = "prepay_id="+wxPrepayVo.getPrepayId();
        signType = "MD5";
        timestamp = genTimeStamp();
        prepayId = wxPrepayVo.getPrepayId();
    }


    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    public String sign(String key){

        StringBuffer singStr = new StringBuffer();
        singStr.append("appId=").append(getAppId());
        singStr.append("&nonceStr=").append(getNonceStr());
        singStr.append("&package=").append(getPackageParam());
        singStr.append("&signType=").append("MD5");
        singStr.append("&timeStamp=").append(getTimestamp());
        singStr.append("&key=").append(key);
        PayService.logger.debug("singStr = [" + singStr.toString() + "]");
        paySign = MD5Util.getMD5(singStr.toString()).toUpperCase();
        PayService.logger.debug("singStr = [" + paySign + "]");
        return paySign;
    }

    public String appSign(String key){
        StringBuffer singStr = new StringBuffer();
        singStr.append("appid=").append(getAppId());
        singStr.append("&noncestr=").append(getNonceStr());
        singStr.append("&package=").append(getPackageParam());
        singStr.append("&partnerid=").append(getPartnerId());
        singStr.append("&prepayid=").append(getPrepayId());
        singStr.append("&timestamp=").append(getTimestamp());
        singStr.append("&key=").append(key);
        PayService.logger.debug("singStr = [" + singStr.toString() + "]");
        paySign = MD5Util.getMD5(singStr.toString()).toUpperCase();
        PayService.logger.debug("singStr = [" + paySign + "]");
        return paySign;
    }






}

