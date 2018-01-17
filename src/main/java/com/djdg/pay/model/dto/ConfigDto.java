package com.djdg.pay.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;

/**
 * Created with IntelliJ IDEA.
 * Description:TODO
 * ConfigDto:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/5
 * Time: 16:09
 */
@Data
public class ConfigDto {

    @NotBlank(message = "微信AppId都不能为空")
    private String appId = "";
    @NotBlank(message = "secret不能为空")
    private String appSecret= "";

    @NotBlank(message = "商户号不能为空")
    private String mchId= "";

    @NotBlank(message = "支付密钥不能为空")
    private String apiKey= "";

    @NotBlank(message = "商品描述不能为空")
    private String body= "";

    private Boolean enableCreditCart= false;

    @NotEmpty(message = "支付回调地址不能为空")
    private String notifyUrl= "";

    @NotEmpty(message = "业务回调地址不能为空")
    private String callbackUrl= "";

    @NotEmpty(message = "业务appId")
    private String businessAppId= "";

    private String clientAppId = "";




}
