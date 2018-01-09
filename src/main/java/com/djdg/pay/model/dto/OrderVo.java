package com.djdg.pay.model.dto;

import com.djdg.pay.db.entity.Order;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * OrderVo:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/8
 * Time: 15:40
 */
@Data
public class OrderVo {
    private Long payOrderId;
    private String openId;
    private String orderNo;
    private String businessOrderId;
    private BigDecimal totalAmount;
    private String businessAppId;


    public OrderVo() {

    }

    public OrderVo(Order order) {
        payOrderId = order.getId();
        BeanUtils.copyProperties(order,this);
    }
}
