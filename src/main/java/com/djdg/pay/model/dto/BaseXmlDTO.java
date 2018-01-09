package com.djdg.pay.model.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @Description:
 * @Auther Demon
 * @Date 2018/1/9 14:16 星期二
 */
@JacksonXmlRootElement(localName = "xml")
public class BaseXmlDTO {

    private SortedMap<String, String> map = new TreeMap<String, String>();

    @JsonAnyGetter
    public SortedMap<String, String> get() {
        return map;
    }

    @JsonAnySetter
    public void set(String name, String value) {
        map.put(name, value);
    }
}
