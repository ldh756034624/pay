package com.djdg.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.djdg.pay.common.PayException;
import com.djdg.pay.common.RedisBean;
import com.djdg.pay.common.Result;
import com.djdg.pay.db.entity.Config;
import com.djdg.pay.db.entity.Order;
import com.djdg.pay.db.repo.ConfigRepository;
import com.djdg.pay.db.repo.OrderRepository;
import com.djdg.pay.model.dto.*;
import com.djdg.pay.model.vo.WxOrderListInfo;
import com.djdg.pay.model.vo.WxPrepayInfo;
import com.djdg.pay.model.vo.WxPrepayVo;
import com.djdg.pay.utils.DateUtil;
import com.djdg.pay.utils.JaxbUtil;
import com.djdg.pay.utils.MD5Util;
import com.djdg.pay.utils.RedisKeyUtil;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:微信支付工具
 * PayService:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/8
 * Time: 14:54
 */
@Service
public class PayService {

    public static Logger logger = LoggerFactory.getLogger(PayService.class);

    @Resource
    private ConfigRepository configRepository;
    @Resource
    private OrderRepository orderRepository;
    @Resource
    private WechatService wechatService;
    @Resource
    private RedisBean redisBean;


    public Result createPrepayOrder(OrderDTO orderDTO
    ) {
        if (orderDTO.getPayMethod() == Order.PayMethodEnum.WXJS.getKey()) {
            String openid = orderDTO.getOpenId();
            if (StringUtils.isEmpty(openid)) {
                return Result.fail("请微信登录", 401);
            }
        }
        Order order = orderRepository.findFirstByBusinessAppIdAndBusinessOrderIdOrderByCreateTimeDesc(orderDTO.getBusinessAppId(), orderDTO.getBusinessOrderId());
        if (order == null) {
            order = new Order();
            // 订单号
            order.setOrderNo(generateOrderNo(orderDTO.getBusinessAppId()));
            order.setBusinessAppId(orderDTO.getBusinessAppId());
            order.setBusinessOrderId(orderDTO.getBusinessOrderId());
            order.setOpenId(orderDTO.getOpenId());
            order.setTotalAmount(orderDTO.getTotalAmount());
            order.setPayMethod(orderDTO.getPayMethod());
            order.setPayStatus(Order.OrderPayStatus.UN_PAY.getValue());
            order = orderRepository.saveAndFlush(order);
        } else {
            order.setPayMethod(orderDTO.getPayMethod());
            order.setOpenId(orderDTO.getOpenId());
            order = orderRepository.saveAndFlush(order);
        }
        return Result.success(new OrderVo(order));
    }


    public Result initWxOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        if (order == null) {
            return Result.fail("获取订单信息失败");
        }

        String businessAppId = order.getBusinessAppId();
        Config config = getConfig(businessAppId);
        if (config == null) {
            return Result.fail("获取支付信息出错");
        }
        PrepayDTO prepayDTO = getPrepayDTO(order, config);
        WxPrepayVo wxPrepayVo = wechatService.getOrder(prepayDTO);
        WxPrepayInfo wxPrepayInfo = new WxPrepayInfo(wxPrepayVo);
        String openId = order.getOpenId();
        boolean isApp =  order.getPayMethod() == Order.PayMethodEnum.WX.getKey();
        if (isApp) {
            wxPrepayInfo.setPackageParam("Sign=WXPay");
            wxPrepayInfo.appSign(config.getClientConfig().getApiKey());
        } else {
            wxPrepayInfo.sign(config.getApiKey());
        }


        return Result.success(wxPrepayInfo);
    }

    public void checkOrder(Long orderId, String businessOrderId, String businessAppId) {
        Order order = orderRepository.findOne(orderId);
        if (order == null) {
            throw new PayException("订单不存在");
        }
        if (!order.getBusinessAppId().equals(businessAppId)
                || !order.getBusinessOrderId().equals(businessOrderId)) {
            logger.error("预支付订单号错误: businessAppId或businessOrderId错误");
            throw new PayException("预支付订单号错误");
        }
        if (order.getPayStatus() != Order.OrderPayStatus.UN_PAY.getValue()) {
            throw new PayException("非待支付订单");
        }
    }

    private String generateOrderNo(String businessAppId) {
        String orderNo = DateUtil.formatDate(new Date(), DateUtil.FormatType.NON_SEPARATOR_SECOND).concat(redisBean.getValueOps().increment(RedisKeyUtil.getOrderSNKey(), 1L).toString());
        String businessName = businessAppId.substring(5);
        businessName = businessName.substring(0, businessName.length() - 5);
        return businessName + orderNo;
    }


    private PrepayDTO getPrepayDTO(Order order, Config config) {
        PrepayDTO prepayDTO = new PrepayDTO();
        String openId = order.getOpenId();

        boolean isApp = order.getPayMethod() == Order.PayMethodEnum.WX.getKey();
        prepayDTO.setBody(config.getBody());
        Boolean enableCreditCart = config.getEnableCreditCart();
        if (!enableCreditCart) prepayDTO.setLimit_pay("no_credit");
        if (!isApp) {
            prepayDTO.setAppid(config.getAppId());
            prepayDTO.setMch_id(config.getMchId());
            prepayDTO.setOpenid(openId);
        } else {
            prepayDTO.setAppid(config.getClientConfig().getAppId());
            prepayDTO.setMch_id(config.getClientConfig().getMchId());

        }
        prepayDTO.setNotify_url(config.getNotifyUrl());
        prepayDTO.setOut_trade_no(order.getOrderNo());
        int amount = order.getTotalAmount().multiply(new BigDecimal("100")).intValue();
        prepayDTO.setTotal_fee(String.valueOf(amount));
        prepayDTO.setTrade_type(isApp ? "APP" : "JSAPI");
        if (!isApp) {
            prepayDTO.sign(config.getApiKey());
        } else {
            prepayDTO.sign(config.getClientConfig().getApiKey());
        }

        return prepayDTO;
    }


    public Config getConfig(String appId) {

        Config config = configRepository.findByBusinessAppId(appId);
        return config;
    }


    public Result refundOrder(RefundDTO refundDTO) {
        Long orderId = null;
        try {
            orderId = Long.valueOf(refundDTO.getOrderId());
        } catch (NumberFormatException e) {
            logger.info(e.getMessage(), e);
            return Result.fail("解析订单编号出错，请确认订单编号");
        }

        Order order = orderRepository.findFirstByBusinessAppIdAndBusinessOrderIdOrderByCreateTimeDesc(refundDTO.getBusinessAppId(),refundDTO.getOrderId());
        if (order == null) {
            return Result.fail("订单不存在");
        }

        Result refundResult = refund(refundDTO,order);

        return refundResult;
    }

    public String getSign(TreeMap<String, Object> params, String payKey) {
        StringBuilder sb = new StringBuilder();

        params.keySet().forEach(key -> {
            sb.append(key);
            sb.append("=");
            sb.append(params.get(key));
            sb.append("&");
        });

        sb.append("key=");
        sb.append(payKey);

        String stringSignTemp = sb.toString();

        String sign = MD5Util.getMD5(stringSignTemp).toUpperCase();
        return sign;
    }

    private String getRequestContent(TreeMap<String, Object> params) {

        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            sb.append("<");
            sb.append(key);
            sb.append(">");

            sb.append(params.get(key));

            sb.append("</");
            sb.append(key);
            sb.append(">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    private Result refund(RefundDTO refundDTO, Order order) {

        Config config = getConfig(order.getBusinessAppId());

        String mchId = "";
        String appId = "";
        if(order.getPayMethod() == Order.PayMethodEnum.WX.getKey()){
             mchId = config.getMchId();
             appId = config.getAppId();
        }else{
             mchId = config.getClientConfig().getMchId();
             appId = config.getClientConfig().getAppId();
        }

        String url = "https://api.mch.weixin.qq.com/secapi/pay/refund";

        TreeMap<String, Object> params = new TreeMap<>();
        params.put("appid", appId);
        params.put("mch_id", mchId);
        params.put("nonce_str", UUID.randomUUID().toString().replace("-", ""));
        params.put("out_trade_no", order.getOrderNo());
        params.put("out_refund_no", UUID.randomUUID().toString().replace("-",""));
        BigDecimal multiply = refundDTO.getTotal_fee().multiply(new BigDecimal(100));
        params.put("total_fee", multiply.intValue());
        params.put("refund_fee", multiply.intValue());
        params.put("sign", getSign(params, refundDTO.getPayKey()));

        String content = getRequestContent(params);
        logger.info("-请求xml 内容： " + content);
        String result = "";
        //指定读取证书格式为PKCS12
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            //读取本机存放的PKCS12证书文件
//            InputStream is = this.getClass().getClassLoader().getResourceAsStream("apiclient_cert.p12");
//            String certPath = "D:\\资料\\红包\\cert\\apiclient_cert.p12";

            InputStream is = new ByteArrayInputStream(refundDTO.getCertByte());

//            InputStream is = new FileInputStream(certPath);
            //指定PKCS12的密码(商户ID)
            keyStore.load(is, mchId.toCharArray());
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchId.toCharArray()).build();
            //指定TLS版本
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            //设置httpclient的SSLSocketFactory
             httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            HttpPost httppost = new HttpPost(url);
            StringEntity reqEntity = new StringEntity(content, "UTF-8");
            httppost.setEntity(reqEntity);
            logger.info("request url: " + httppost.getRequestLine());
            response = httpclient.execute(httppost);
            result = EntityUtils.toString(response.getEntity(), "UTF-8");

            JAXBContext jc = JAXBContext.newInstance(RefundResult.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
//            RefundResult refundResult = (RefundResult) unmarshaller.unmarshal(new StringReader(result));
            RefundResult refundResult = RefundResult.parseFromXML(result);
            logger.info("-退款结果： " + JSON.toJSON(refundResult));
            boolean refund = false;
            if ("SUCCESS".equals(refundResult.getResult_code())) {
                //退款成功
                logger.info("-退款成功: ");
                return Result.success();
            } else {
                logger.info("-失败原因： ");
                //退款失败
                return Result.success();

            }

        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            return Result.fail("退款失败");
        }finally {
            try {
                response.close();
            } catch (Exception e) {
                logger.info("关闭流 response 失败");
                logger.info(e.getMessage(),e);
            }finally {
                response = null;
            }

            try {
                httpclient.close();
            } catch (Exception e) {
                logger.info("关闭httpClient 失败");
                logger.info(e.getMessage(),e);
            }finally {
                httpclient = null;
            }
        }
    }

    public static void main(String[] args) throws JAXBException {


        String result = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "\n" +
                "<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>  \n" +
                "  <return_msg><![CDATA[OK]]></return_msg>  \n" +
                "  <appid><![CDATA[wxeaaec76c38776768]]></appid>  \n" +
                "  <mch_id><![CDATA[1385978802]]></mch_id>  \n" +
                "  <nonce_str><![CDATA[KVDUcQH0UN3m6z4O]]></nonce_str>  \n" +
                "  <sign><![CDATA[2CD65148BC7BE83C179DD43C6444BB42]]></sign>  \n" +
                "  <result_code><![CDATA[FAIL]]></result_code>  \n" +
                "  <err_code><![CDATA[ORDERNOTEXIST]]></err_code>  \n" +
                "  <err_code_des><![CDATA[订单不存在]]></err_code_des> \n" +
                "</xml>\n";
        RefundResult refundResult = RefundResult.parseFromXML(result);
        System.out.println(JSONObject.toJSONString(refundResult));
    }

    public Result getOrderInfoByNo(String no) {
        Order order = orderRepository.findByTransactionId(no);
        if(order == null){
            return Result.fail("订单不在存");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("payInfoId", order.getBusinessOrderId());
        map.put("wxId", order.getTransactionId());
        return Result.success(map);
    }
}
