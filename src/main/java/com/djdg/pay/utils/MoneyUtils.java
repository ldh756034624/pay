package com.djdg.pay.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by itservice on 2017/11/18.
 */
public class MoneyUtils {

    private MoneyUtils(){}

    public static  String formatMoney(BigDecimal money){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(money);
    }

    public static String formatMoney(BigDecimal money,String pattern){
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(money);
    }

    public static void main(String[] args) {
        BigDecimal de = new BigDecimal("30.01");

        System.out.println(formatMoney(de, "0"));
    }
}
