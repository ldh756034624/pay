package com.djdg.pay.model.vo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sun.xml.internal.txw2.annotation.XmlNamespace;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * @Description:
 * @Auther Demon
 * @Date 2018/1/9 14:19 星期二
 */
@JacksonXmlRootElement(localName = "xml")
public class WxPayResponseVO implements Serializable {
    @XmlElement
    private String return_code;
    @XmlElement
    private String return_msg;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }
    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }


}
