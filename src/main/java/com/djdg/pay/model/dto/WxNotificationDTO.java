package com.djdg.pay.model.dto;

import com.djdg.pay.db.entity.Order;
import lombok.Data;

import java.util.SortedMap;

/**
 * @Description: 微信支付回调参数
 * @Auther Demon
 * @Date 2018/1/9 15:08 星期二
 */
@Data
public class WxNotificationDTO {

    private String device_info;
    private String cash_fee_type;
    private String fee_type;
    private String is_subscribe;
    private String return_code;
    private String return_msg;
    // 公众账号ID
    private String appid;
    // 商户号
    private String mch_id;
    // 随机字符串
    private String nonce_str;
    // 签名
    private String sign;
    private String result_code;
    private String err_code;
    private String err_code_des;
    // 用户标识
    private String openid;
    // 交易类型
    private String trade_type;
    // 付款银行
    private String bank_type;
    // 总金额
    private Integer total_fee; // 订单总金额，单位为分
    // 现金支付金额
    private Integer cash_fee; // 现金支付金额订单现金支付金额，详见支付金额
    // 代金券或立减优惠金额
    private Integer coupon_fee; // 代金券或立减优惠金额<=订单总金额，订单总金额-代金券或立减优惠金额=现金支付金额，详见支付金额
    private Integer coupon_count; // 代金券或立减优惠使用数量
    // 微信支付订单号
    private String transaction_id;
    // 商户订单号
    private String out_trade_no;
    // 支付完成时间
    private String time_end; // 20141030133525
    private SortedMap<String, String> notify_params;

}
