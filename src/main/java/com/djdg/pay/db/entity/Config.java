package com.djdg.pay.db.entity;

import com.djdg.pay.db.BaseEntity;
import lombok.Data;

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
@Table(name = "Config")
public class Config extends BaseEntity {


    @Id
    @SequenceGenerator(name = "paySeq", sequenceName = "pay_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = IDENTITY, generator = "paySeq")
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(64) default '' COMMENT '子项目名称'")
    private String name;

    @Column(name = "app_id", nullable = false, columnDefinition = "varchar(64) default '' COMMENT 'appId'")
    private String appId;
    
    @Column(name = "callback_url", nullable = false, columnDefinition = "varchar(255) default '' COMMENT '回调地址'")
    private String callBackUrl;

    @Column(name = "secret_key", nullable = false, columnDefinition = "varchar(128) default '' COMMENT '加密key'")
    private String secretKey;

    @Column(name = "callback_status",nullable = false,columnDefinition = "int default 0 COMMENT '是否直接回调'")
    private Integer callbackStatus;





}
