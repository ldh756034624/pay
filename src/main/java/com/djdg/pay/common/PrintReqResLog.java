package com.djdg.pay.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description: 是否打印请求和响应日志
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface PrintReqResLog {

    boolean printRequestParams() default false;

    boolean printResponseResult() default false;

}
