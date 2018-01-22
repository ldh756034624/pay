package com.djdg.pay.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * Created by ldh on 2017/7/29.
 */
@Data
@Accessors(chain = true)
@XmlRootElement(namespace = "xml")
public class RefundResult {

    private String return_code;
    private String return_msg;
    private String appid;
    private String mch_id;
    private String nonce_str;
    private String sign;
    private String result_code;
    private String err_code;
    private String err_code_des;

    public static RefundResult parseFromXML(String xml) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(xml);

            Element rootElement = document.getRootElement();

            Iterator<Element> iterator = rootElement.elementIterator();

            RefundResult refundResult = new RefundResult();

            while (iterator.hasNext()) {

                Element element = iterator.next();
                String key = element.getName();

                Class<? extends RefundResult> clazz = refundResult.getClass();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    String name = field.getName();
                    if (key.equals(name)) {
                        field.set(refundResult, element.getText());
                        continue;
                    }
                }
            }

            return refundResult;

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
