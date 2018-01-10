package com.djdg.pay.model.dto;

import com.djdg.pay.db.entity.Order;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
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

    private String openId ="";
    @NotBlank(message = "请传入订单号")
    private String orderNo = "";
    @NotBlank(message = "请传入业务订单id")
    private String businessOrderId  = "" ;
    @NotNull(message = "请传入支付金额")
    private BigDecimal totalAmount = new BigDecimal(0);
    @NotBlank(message = "请传入业务Appid")
    private String businessAppId = "" ;

    private int payMethod = Order.PayMethodEnum.WXJS.getKey();



}
