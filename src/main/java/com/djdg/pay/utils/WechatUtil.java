package com.djdg.pay.utils;

import com.djdg.pay.db.entity.Config;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.SortedMap;

/**
 * @Description:
 * @Auther Demon
 * @Date 2018/1/9 15:23 星期二
 */
public class WechatUtil {

    private static Logger logger = LoggerFactory.getLogger(WechatUtil.class);

    public final static String SUCCESS = "SUCCESS";
    public final static String FAILED = "FAIL";

    public static boolean verifySign(Config paymentConfig, SortedMap<String, String> map) {
        logger.debug("verifySign {}, apiKey is {}", map, paymentConfig.getApiKey());

        if (!StringUtils.equals(paymentConfig.getMchId(), map.get("mch_id"))
                || !StringUtils.equals(paymentConfig.getAppId(), map.get("appid"))) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (StringUtils.isNotBlank(value) && !(StringUtils.equals(key, "sign") || StringUtils.equals(key, "formDataSource"))) {
                sb.append(key);
                sb.append('=');
                sb.append(value);
                sb.append('&');
            }
        }
        sb.append("key=").append(paymentConfig.getApiKey());
        logger.debug("=====sb.toString()====" + sb.toString());
        String signNew = MD5Util.getMD5(sb.toString()).toUpperCase();
        logger.debug("signNew is {}, sign from wx is {}", signNew, map.get("sign"));
        return StringUtils.equals(signNew, map.get("sign"));
    }

}
