package com.djdg.pay.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * Created by ldh on 2017/8/10.
 */
@Data
@Accessors(chain = true)
public class RefundDTO {
    private String outTradeNo;

    private BigDecimal total_fee;

    private String mchId;

    private String appId;

    private String payKey;

    private String orderId;

    private String businessAppId;

//    private String cert;

    private byte[] certByte;
}
