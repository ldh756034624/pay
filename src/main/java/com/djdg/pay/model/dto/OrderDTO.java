package com.djdg.pay.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * OrderDTO:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/8
 * Time: 15:03
 */
@Data
public class OrderDTO {

    private String openId;

    private String orderNo;

    private String businessOrderId;

    private BigDecimal totalAmount;

    private String businessAppId;



}
