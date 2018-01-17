package com.djdg.pay.model.dto;

import lombok.Data;

import javax.persistence.Column;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * ClientConfig:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/17
 * Time: 14:59
 */
@Data
public class ClientConfig {

    private String appId;
    private String appSecret;
    private String mchId;
    private String apiKey;

}
