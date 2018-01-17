package com.djdg.pay.db.entity;

import com.djdg.pay.db.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/5
 * Time: 15:29
 */
@Data
@Entity
@DynamicInsert
@Table(name = "payment_config")
public class Config extends BaseEntity {

    @Id
    @SequenceGenerator(name = "paySeq", sequenceName = "pay_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = IDENTITY, generator = "paySeq")
    private Long id;

    @Column(name = "app_id", columnDefinition = "VARCHAR(255) COMMENT 'appid'", nullable = false)
    private String appId;

    @Column(name = "app_secret", columnDefinition = "VARCHAR(255) COMMENT 'secret'")
    private String appSecret;

    @Column(name = "mch_id", columnDefinition = "VARCHAR(255) COMMENT '商户号'", nullable = false)
    private String mchId;

    @Column(name = "api_key", columnDefinition = "VARCHAR(255) COMMENT '支付密钥'", nullable = false)
    private String apiKey;

    @Column(name = "body", columnDefinition = "VARCHAR(255) COMMENT '商品描述'", nullable = false)
    private String body;

    @Column(name = "enable_credit_cart", columnDefinition = "BIT(1) DEFAULT 0 COMMENT '是否可用信用卡'", nullable = false)
    private Boolean enableCreditCart;

    @Column(name = "notify_url", columnDefinition = "VARCHAR(255) COMMENT '支付回调地址'", nullable = false)
    private String notifyUrl;

    @Column(name = "callback_url", columnDefinition = "VARCHAR(255) COMMENT '业务回调地址'")
    private String callbackUrl;

    @Column(name = "business_app_id", columnDefinition = "VARCHAR(32) COMMENT '业务appId'")
    private String businessAppId;

    @Column(name = "status", columnDefinition = "SMALLINT DEFAULT 0 COMMENT '状态'", nullable = false)
    protected Integer status;

    @Column(name = "client_app_id", nullable = false, columnDefinition = "varchar(64) default '' COMMENT 'android ios 支付appid'")
    private String clientAppId;




}
