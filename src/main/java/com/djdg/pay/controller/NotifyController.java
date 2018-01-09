package com.djdg.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.djdg.pay.model.dto.BaseXmlDTO;
import com.djdg.pay.model.dto.WxNotificationDTO;
import com.djdg.pay.model.vo.WxPayResponseVO;
import com.djdg.pay.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.SortedMap;

/**
 * @Description:
 * @Auther Demon
 * @Date 2018/1/9 10:57 星期二
 */
@RestController
@RequestMapping("notify")
public class NotifyController {

    @Autowired
    private NotifyService notifyService;

    /**
     *
     * @param baseXmlDTO
     * @param payMethod 支付方式 wx/wxjs
     * @param businessAppId
     * @return
     */
    @PostMapping(value = "wx/{payMethod}/{businessAppId}", produces = "text/xml;charset=utf-8")
    public WxPayResponseVO wxNotify(@RequestBody BaseXmlDTO baseXmlDTO, @PathVariable("payMethod") String payMethod, @PathVariable("businessAppId") String businessAppId) {
        SortedMap<String, String> notifyParams = baseXmlDTO.get();
        WxNotificationDTO notification = JSONObject.parseObject(JSONObject.toJSONString(notifyParams), WxNotificationDTO.class);
        notification.setNotify_params(notifyParams);
        return notifyService.processWxNotification(notification, payMethod, businessAppId);
    }

}
