package com.djdg.pay.db.entity;

import com.djdg.pay.db.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @Description: 订单信息
 * @Auther Demon
 * @Date 2017/12/7 17:14 星期四
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "order_info", uniqueConstraints = {@UniqueConstraint(columnNames="order_no")})
public class Order extends BaseEntity {

    @Id
    @SequenceGenerator(name = "paySeq", sequenceName = "pay_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = IDENTITY, generator = "paySeq")
    private Long id;

    @Column(name = "order_no", columnDefinition = "VARCHAR(255) COMMENT '支付订单号'", nullable = false)
    private String orderNo;

    @Column(name = "business_order_id", columnDefinition = "VARCHAR(255) COMMENT '业务订单号'")
    private String businessOrderId;

    @Column(name = "pay_status", columnDefinition = "SMALLINT DEFAULT 0 COMMENT '支付状态:0未支付，1已支付'", nullable = false)
    protected Integer payStatus;

    @Column(name = "total_amount", columnDefinition = "DECIMAL(12, 2) DEFAULT 0.00 COMMENT '总金额'")
    private BigDecimal  totalAmount;
    
    @Column(name = "open_id", nullable = false, columnDefinition = "varchar(64) default '' COMMENT '支付时的openId'")
    private String openId;

    @Column(name = "transaction_id", columnDefinition = "VARCHAR(512) COMMENT '第三方支付流水号'")
    private String transactionId;

    @Column(name = "notify_log", columnDefinition = "VARCHAR(512) COMMENT '支付回调原始记录'")
    private String notifyLog;

    @Column(name = "business_app_id", columnDefinition = "VARCHAR(32) COMMENT '业务appId'")
    private String businessAppId;

    @Column(name = "status", columnDefinition = "SMALLINT DEFAULT 0 COMMENT '状态:0默认，1已回调业务系统'", nullable = false)
    protected Integer status;



    public static enum OrderPayStatus {

        UN_PAY(0, "待支付"),
        PAID(1, "已支付");

        private int value;
        private String desc;

        OrderPayStatus(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }


    public enum PayMethodEnum {
        BALANCE(0, "balance"), ALIPAY(1, "alipay"), WX(2, "wx"), WXJS(3, "wxjs"), YWT(4, "ywt"),
        WXSCAN(5, "wxscan"), ALISCAN(6, "aliscan"), WXMINI(7, "wxmini"), ALINATIVE(8, "alinative");

        private int key;
        private String value;

        public static PayMethodEnum valueOf(int key) {
            switch (key) {
                case 0:
                    return BALANCE;
                case 1:
                    return ALIPAY;
                case 2:
                    return WX;
                case 3:
                    return WXJS;
                case 4:
                    return YWT;
                case 5:
                    return WXSCAN;
                case 6:
                    return ALISCAN;
                case 7:
                    return WXMINI;
                case 8:
                    return ALINATIVE;
                default:
                    return null;
            }
        }

        public static PayMethodEnum getByValue(String value) {
            PayMethodEnum[] season = values();
            for (PayMethodEnum s : season) {
                if (s.getValue().equals(value)) {
                    return s;
                }
            }
            return null;
        }

        public static boolean contains(int key) {
            //所有的枚举值
            PayMethodEnum[] season = values();
            //遍历查找
            for (PayMethodEnum s : season) {
                if (s.getKey() == key) {
                    return true;
                }
            }
            return false;
        }

        public static boolean contains(String value) {
            //所有的枚举值
            PayMethodEnum[] season = values();
            //遍历查找
            for (PayMethodEnum s : season) {
                if (s.getValue().equals(value)) {
                    return true;
                }
            }
            return false;
        }

        private PayMethodEnum(int key, String value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }


    }




}
