package com.djdg.pay.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by Ln on 2018/3/6.
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class WxOrderListInfo {
    private String wxOrderNo;

    private String orderType;

    private String orderId;

    private String money;

    private String createTime;

    private String payDate;
}
