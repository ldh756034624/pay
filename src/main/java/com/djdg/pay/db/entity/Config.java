package com.djdg.pay.db.entity;

import com.alibaba.fastjson.JSONObject;
import com.djdg.pay.db.BaseEntity;
import com.djdg.pay.model.dto.ClientConfig;
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
    
    
    @Column(name = "client_config", nullable = false, columnDefinition = "varchar(1536) default '' COMMENT '客户端配置'")
    private String clientConfig;


    public ClientConfig getClientConfig() {
        try {
            ClientConfig clientConfig = JSONObject.parseObject(this.clientConfig, ClientConfig.class);
            return clientConfig;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setClientConfig(ClientConfig clientConfig) {

        this.clientConfig = JSONObject.toJSONString(clientConfig);;
    }
}
