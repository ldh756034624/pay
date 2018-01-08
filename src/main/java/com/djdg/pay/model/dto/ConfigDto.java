package com.djdg.pay.model.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

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
    @NotBlank(message = "请填入项目名称")
    private String name;
    @NotBlank(message = "请填入appId,请注意要全局唯一")
    private String appId;
    @NotBlank(message = "请填入微信AppId,请注意要全局唯一")
    private String wxAppId;
    @NotBlank(message = "请填入微信密钥,请注意要全局唯一")
    private String wxSecretKey;
    @NotBlank(message = "请填入回调地址")
    private String callBackUrl;

    private int callbackStatus;




}
