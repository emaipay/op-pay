package com.hitler.payservice;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.hitler.util.*;
import net.sf.json.JSONObject;
import org.jdom.JDOMException;

import com.alibaba.fastjson.JSON;
import com.hitler.core.Constants;
import com.hitler.core.dto.ResultDTO;
import com.hitler.core.exception.ThirdPayBussinessException;
import com.hitler.core.log.PayLog;
import com.hitler.core.utils.DateUtil;
import com.hitler.core.utils.StringUtils;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayOrder;
import com.hitler.payservice.support.IpayService;
import com.hitler.payservice.support.PayOrderQueryData;

public class WxPayService implements IpayService {

    public static final String CALLBACK_PAGE_PATH = "/wx/callback-page";
    public static final String CALLBACK_DATA_PATH = "/wx/callback-data";

    @Override
    public Map<String, Object> getPayData(HttpServletRequest request,
                                          PayOrder order, String payCode) {
        Map<String, Object> respMap = new HashMap<String, Object>();
        switch (payCode){
            case "APP":
                respMap.put("redirect", 0);
                respMap.put("postUrl", appPayHandle(request, order, payCode));
                break;
            case "MWEB":
                respMap.put("redirect", 1);
                respMap.put("postUrl", h5PayHandle(request, order, payCode));
                break;
            case "JSAPI":
                ResultDTO<SortedMap<String, String>> dto = wapPayHandle(request, order, payCode);
                respMap.put("redirect", 2);
                respMap.put("postUrl", dto);
                break;
            case "NATIVE":
                respMap.put("redirect", 3);
                respMap.put("target", 1);//表示微信扫码支付
                respMap.put("postUrl", scanPayHandle(request, order, payCode));
                break;
        }
        return respMap;
    }

    private String getPayResult(HttpServletRequest request, PayOrder order, String payCode, PayMerchant pm, String randomStr) throws Exception {
        //String call = request.getScheme() + "://" + request.getServerName();
        String call = "http://pay.woyao518.com";
        String notifyUrl = call + "/pay/callback" + CALLBACK_DATA_PATH;
        PayLog.getLogger().info("notifyUrl:" + notifyUrl);
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("appid", pm.getTerminalNo());//公众账号ID appid
        parameters.put("mch_id", pm.getMerchantNo());//商户号 mch_id
        parameters.put("nonce_str", randomStr);
        parameters.put("body", "商城充值");
        parameters.put("out_trade_no", order.getTransBillNo()); // 订单id
        // parameters.put("attach", "test");
        parameters.put("total_fee", Math.round(order.getOrderAmount() * 100) + "");
        String ip = com.hitler.util.RequestUtils.getIpAddress(request);
        PayLog.getLogger().info("ip：" + ip);
        parameters.put("spbill_create_ip", ip);
        parameters.put("notify_url", notifyUrl);
        parameters.put("trade_type", payCode);
        if (payCode.equals("JSAPI")) {//公众号支付
            parameters.put("openid", order.getOpenid());
        }
        if (payCode.equals("MWEB")) {//H5支付
            JSONObject h5Info = new JSONObject();
            h5Info.put("type", "Wap");
            h5Info.put("wap_url", "http://m.woyao518.com");
            h5Info.put("wap_name", "商城充值");
            JSONObject json = new JSONObject();
            json.put("h5_info", h5Info);
            parameters.put("scene_info", json.toString());
        }

        //	nonce_str 是 String(32) 5K8264ILTKCH16CQ2502SI8ZNMTM67VS 随机字符串，不长于32位。推荐随机数生成算法
        //	签名 sign 是 String(32) C380BEC2BFD727A4B6845133519F3AD6 签名，详见签名生成算法
        //	商品描述 body 是 String(128
        //商户订单号 out_trade_no 是  商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
        //总金额 total_fee 是 Int 888 订单总金额，单位为分，详见支付金额
        //终端IP spbill_create_ip 是 String(16) 123.12.12.123 必须传正确的用户端IP,详见获取用户ip指引
        //通知地址 notify_url 是 String(256) http://www.weixin.qq.com/wxpay/pay.php 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
        //交易类型 trade_type 是 String(16) MWEB H5支付的交易类型为MWEB
//场景信息 scene_info 是 String(256)
//IOS移动应用{"h5_info": {"type":"IOS","app_name": "王者荣耀","bundle_id": "com.tencent.wzryIOS"}}
//安卓移动应用{"h5_info": {"type":"Android","app_name": "王者荣耀","package_name": "com.tencent.tmgp.sgame"}}
//WAP网站应用{"h5_info": {"type":"Wap","wap_url": "http://m.woyao518.com","wap_name": "腾讯充值"}}
/* <xml>
<appid>wx2421b1c4370ec43b</appid>
<attach>支付测试</attach>
<body>H5支付测试</body>
<mch_id>10000100</mch_id>
<nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str>
<notify_url>http://wxpay.wxutil.com/pub_v2/pay/notify.v2.php</notify_url>
<openid>oUpF8uMuAJO_M2pxb1Q9zNjWeS6o</openid>
<out_trade_no>1415659990</out_trade_no>
<spbill_create_ip>14.23.150.211</spbill_create_ip>
<total_fee>1</total_fee>
<trade_type>MWEB</trade_type>
<scene_info>{"h5_info": {"type":"IOS","app_name": "王者荣耀","package_name": "com.tencent.tmgp.sgame"}}</scene_info>
<sign>0CB01533B8C1EF103065174F50BCA001</sign>
</xml>*/
        // 设置签名
        //String sign = PayCommonUtil.getSign(parameters, pm.getKey());
        String sign = PayCommonUtil.generateSignature(parameters, pm.getKey());
        parameters.put("sign", sign);
        // 封装请求参数结束
        String requestXML = PayCommonUtil.getRequestXml(parameters);
        // 调用统一下单接口
        PayLog.getLogger().info("微信支付" + payCode + "请求报文：" + requestXML);//https://api.mch.weixin.qq.com/pay/unifiedorder
        String result = HttpUtil.Post("https://api.mch.weixin.qq.com/pay/unifiedorder", requestXML).getHtml();
        PayLog.getLogger().info("微信支付" + payCode + "响应报文：" + result);
        return result;
    }


    @SuppressWarnings("unchecked")
    private ResultDTO<SortedMap<String, String>> wapPayHandle(HttpServletRequest request, PayOrder order, String payCode) {
        if (StringUtils.isBlank(order.getOpenid())) {
            return ResultDTO.error(Constants.PAY_FAIL, "公众号支付openid不能为空");
        }
        PayMerchant pm = order.getMerchantId();
        String randomStr = PayCommonUtil.generateUUID();

        try {
            String result = getPayResult(request, order, payCode, pm, randomStr);
            Map<String, String> map = XMLUtil.doXMLParse(result);
            if (map.get("return_code").equals("FAIL")) {
                String returnMsg = map.get("return_msg");
                return ResultDTO.error(Constants.PAY_FAIL, StringUtils.isBlank(returnMsg) ? "签名失败" : returnMsg);
            }
            if (map.get("result_code").equals("FAIL")) {
                String err_code_des = map.get("err_code_des");
                return ResultDTO.error(Constants.PAY_FAIL, err_code_des);
            }
			/*公众号id 	appId 	是 	String(16) 	wx8888888888888888 	商户注册具有支付权限的公众号成功后即可获得
			时间戳 	timeStamp 	是 	String(32) 	1414561699 	当前的时间，其他详见时间戳规则
			随机字符串 	nonceStr 	是 	String(32) 	5K8264ILTKCH16CQ2502SI8ZNMTM67VS 	随机字符串，不长于32位。推荐随机数生成算法
			订单详情扩展字符串 	package 	是 	String(128) 	prepay_id=123456789 	统一下单接口返回的prepay_id参数值，提交格式如：prepay_id=***
			签名方式 	signType 	是 	String(32) 	MD5 	签名类型，默认为MD5，支持HMAC-SHA256和MD5。注意此处需与统一下单的签名类型一致
			签名 	paySign 	是 	String(64) 	C380BEC2BFD727A4B6845133519F3AD6 	签名，详见签名生成算法*/
            //列表中参数名区分大小，大小写错误签名验证会失败。
            SortedMap<String, String> parameterMap2 = new TreeMap<String, String>();
            parameterMap2.put("appId", pm.getTerminalNo());
            //parameterMap2.put("partnerid", pm.getMerchantNo());
            //parameterMap2.put("prepayid", map.get("prepay_id"));
            parameterMap2.put("package", "prepay_id=" + map.get("prepay_id"));
            parameterMap2.put("nonceStr", randomStr);
            // 本来生成的时间戳是13位，但是ios必须是10位，所以截取了一下
            parameterMap2.put("timeStamp", (System.currentTimeMillis() / 1000) + "");
            parameterMap2.put("signType", "MD5");
            //String sign2 = PayCommonUtil.getSign(parameterMap2, pm.getKey());
            String sign2 = PayCommonUtil.generateSignature(parameterMap2, pm.getKey());
            parameterMap2.put("paySign", sign2);

            return ResultDTO.success(parameterMap2, "成功");
        } catch (Exception e) {
            PayLog.getLogger().error("wapPayHandle Exception:" + e.getMessage(), e);
            return ResultDTO.error(Constants.PAY_FAIL, e.getMessage());
        }
    }

    /**
     * 扫码支付
     *
     * @param request
     * @param order
     * @param payCode
     * @return
     */
    @SuppressWarnings("unchecked")
    private String scanPayHandle(HttpServletRequest request, PayOrder order, String payCode) {
        PayMerchant pm = order.getMerchantId();
        String randomStr = PayCommonUtil.generateUUID();
        try {
            String result = getPayResult(request, order, payCode, pm, randomStr);
            Map<String, String> map = XMLUtil.doXMLParse(result);
            if (map.get("return_code").equals("FAIL")) {
                String returnMsg = map.get("return_msg");
                PayLog.getLogger().error("return_code FAIL:" + (StringUtils.isBlank(returnMsg) ? "签名失败" : returnMsg));
                return null;
            }
            if (map.get("result_code").equals("FAIL")) {
                String err_code_des = map.get("err_code_des");
                PayLog.getLogger().error("result_code FAIL:" + err_code_des);
                return null;
            }
            //二维码链接
            return  map.get("code_url");
        } catch (Exception e) {
            PayLog.getLogger().error("wapPayHandle Exception:" + e.getMessage(), e);
            return JSON.toJSONString(ResultDTO.error(Constants.PAY_FAIL, e.getMessage()));
        }
    }

    private String h5PayHandle(HttpServletRequest request, PayOrder order, String payCode) {
        PayMerchant pm = order.getMerchantId();
        String randomStr = PayCommonUtil.generateUUID();
        /*公众账号ID appid 是 String(32) wx8888888888888888 调用接口提交的公众账号ID
        商户号 mch_id 是 String(32) 1900000109 调用接口提交的商户号
        设备号 device_info 否 String(32) 013467007045764 调用接口提交的终端设备号，
        随机字符串 nonce_str 是 String(32) 5K8264ILTKCH16CQ2502SI8ZNMTM67VS 微信返回的随机字符串
        签名 sign 是 String(32) C380BEC2BFD727A4B6845133519F3AD6 微信返回的签名，详见签名算法
        业务结果 result_code 是 String(16) SUCCESS SUCCESS/FAIL
        错误代码 err_code 否 String(32) SYSTEMERROR 详细参见错误列表
        错误代码描述 err_code_des 否 String(128) 系统错误 错误返回的信息描述

        以下字段在return_code 和result_code都为SUCCESS的时候有返回
        字段名 变量名 必填 类型 示例值 描述
        交易类型 trade_type 是 String(16) MWEB 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，,H5支付固定传MWEB
        预支付交易会话标识 prepay_id 是 String(64) wx201410272009395522657a690389285100 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时,针对H5支付此参数无特殊用途
        支付跳转链接 mweb_url 是 String(64) https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx2016121516420242444321ca0631331346&package=1405458241  mweb_url为拉起微信支付收银台的中间页面，可通过访问该url来拉起微信客户端，完成支付,mweb_url的有效期为5分钟
*/
        try {
            String result = getPayResult(request, order, payCode, pm, randomStr);
            Map<String, String> map = XMLUtil.doXMLParse(result);
            if (map.get("return_code").equals("FAIL")) {
                String returnMsg = map.get("return_msg");
                PayLog.getLogger().error("return_code FAIL:" + (StringUtils.isBlank(returnMsg) ? "签名失败" : returnMsg));
                return null;
            }
            if (map.get("result_code").equals("FAIL")) {
                String err_code_des = map.get("err_code_des");
                PayLog.getLogger().error("result_code FAIL:" + err_code_des);
                return null;
            }
            String call = "http://pay.woyao518.com";
            String retUrl = call + "/pay/callback" + CALLBACK_PAGE_PATH + "?out_trade_no=" + order.getTransBillNo();
            //String retUrl = "http://m.woyao518.com";
            String urlString = URLEncoder.encode(retUrl, "UTF-8");
            String mweb_url = map.get("mweb_url") + "&redirect_url=" + urlString;
            //String mweb_url = map.get("mweb_url");
            return mweb_url;//支付跳转链接
        } catch (Exception e) {
            PayLog.getLogger().error("wapPayHandle Exception:" + e.getMessage(), e);
            return null;
        }
    }


    @SuppressWarnings("unchecked")
    private String appPayHandle(HttpServletRequest request, PayOrder order, String payCode) {
        PayMerchant pm = order.getMerchantId();
        String randomStr = PayCommonUtil.generateUUID();

        try {
            String result = getPayResult(request, order, payCode, pm, randomStr);
            Map<String, String> map = XMLUtil.doXMLParse(result);
            if (map.get("return_code").equals("FAIL")) {
                String returnMsg = map.get("return_msg");
                return JSON.toJSONString(ResultDTO.error(Constants.PAY_FAIL, StringUtils.isBlank(returnMsg) ? "签名失败" : returnMsg));
            }
            if (map.get("result_code").equals("FAIL")) {
                String err_code_des = map.get("err_code_des");
                return JSON.toJSONString(ResultDTO.error(Constants.PAY_FAIL, err_code_des));
            }
            /**
             * 统一下单接口返回正常的prepay_id，再按签名规范重新生成签名后，将数据传输给APP。参与签名的字段名为appId，
             * partnerId，prepayId，nonceStr，timeStamp，package。注意：package的值格式为Sign=WXPay
             *
             **/

            //	PayLog.getLogger().info(JSON.toJSONString(map));
            SortedMap<String, String> parameterMap2 = new TreeMap<String, String>();
            parameterMap2.put("appid", pm.getTerminalNo());
            parameterMap2.put("partnerid", pm.getMerchantNo());
            parameterMap2.put("prepayid", map.get("prepay_id"));
            parameterMap2.put("package", "Sign=WXPay");
            //parameterMap2.put("package", "Sign");
            parameterMap2.put("noncestr", randomStr);
            // 本来生成的时间戳是13位，但是ios必须是10位，所以截取了一下
            Integer timeStamp = (int) (System.currentTimeMillis() / 1000);
            parameterMap2.put("timestamp", timeStamp + "");
            String key = pm.getKey();
            //String sign2 = PayCommonUtil.getSign(parameterMap2, key);
            String sign2 = PayCommonUtil.generateSignature(parameterMap2, key);
            parameterMap2.put("sign", sign2);
            Map<String, Object> respMap = new HashMap<String, Object>();
            respMap.put("success", true);// 操作结果,true-成功;false-失败
            respMap.put("respCode", Constants.SUCCESS);// 返回编码
            respMap.put("respMsg", "成功");// 返回消息
            respMap.put("params", parameterMap2);// App调起支付所需参数
            return JSON.toJSONString(respMap);
        } catch (Exception e) {
            PayLog.getLogger().error("appPayHandle Exception:" + e.getMessage(), e);
            return JSON.toJSONString(ResultDTO.error(Constants.PAY_FAIL, e.getMessage()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getPayOrderNo(HttpServletRequest request) {
        try {
            String notifyXml = HttpReqUtil.inputStreamToStrFromByte(request.getInputStream());
            PayLog.getLogger().info("getPayOrderNo微信支付系统发送的数据" + notifyXml);
            if (StringUtils.isBlank(notifyXml)) {
                return request.getParameter("out_trade_no");
            }
            Map<String, String> paraMap = XMLUtil.doXMLParse(notifyXml);
            String out_trade_no = paraMap.get("out_trade_no");
            request.setAttribute("paraMap", paraMap);
            request.setAttribute("notifyXml", notifyXml);
            return out_trade_no;
        } catch (IOException e) {
            PayLog.getLogger().error("getPayOrderNo IOException:" + e.getMessage(), e);
        } catch (JDOMException e) {
            PayLog.getLogger().error("getPayOrderNo JDOMException:" + e.getMessage(), e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void checkPayBackData(HttpServletRequest request, PayOrder order)
            throws Exception {
        PayMerchant pm = order.getMerchantId();
        PayLog.getLogger().info("开始处理支付返回的请求");

        //String notifyXml = HttpReqUtil.inputStreamToStrFromByte(request.getInputStream());
        String notifyXml = (String) request.getAttribute("notifyXml");
        PayLog.getLogger().info("checkPayBackData微信支付系统发送的数据" + notifyXml);

        // 微信支付系统发送的数据（<![CDATA[product_001]]>格式）
        //Map<String, String> paraMap = XMLUtil.doXMLParse(notifyXml);
        Map<String, String> paraMap = (Map<String, String>) request.getAttribute("paraMap");
        String appId = paraMap.get("appid");
        if (!appId.equals(pm.getTerminalNo())) {
            PayLog.getLogger().error("appid不匹配，订单放弃:" + paraMap);
            throw new ThirdPayBussinessException("appid不匹配，订单放弃");
        }
        if (!paraMap.get("result_code").equals("SUCCESS")) {
            PayLog.getLogger().error("订单交易失败:" + paraMap);
            throw new ThirdPayBussinessException("订单交易失败");
        }
        String sign = PayCommonUtil.getSign(paraMap, pm.getKey());
        if (!sign.equals(paraMap.get("sign"))) {
            PayLog.getLogger().error("签名校验不通过，订单放弃:" + paraMap);
            throw new ThirdPayBussinessException("签名校验不通过，订单放弃");
        }
        String transactionId = paraMap.get("transaction_id");
        order.setTransactionId(transactionId);
    }

    @Override
    public PayOrderQueryData queryOrder(HttpServletRequest request,
                                        PayOrder order) throws Exception {
        PayMerchant pm = order.getMerchantId();
        String randomStr = JUUIDUtil.createUuid();
        SortedMap<String, String> parameters = new TreeMap<String, String>();
		/*公众账号ID 	appid 	是 	String(32) 	wxd678efh567hg6787 	微信支付分配的公众账号ID（企业号corpid即为此appId）
		商户号 	mch_id 	是 	String(32) 	1230000109 	微信支付分配的商户号
		微信订单号 	transaction_id 	二选一 	String(32) 	1009660380201506130728806387 	微信的订单号，建议优先使用
		商户订单号 	out_trade_no 	String(32) 	20150806125346 	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。 详见商户订单号
		随机字符串 	nonce_str 	是 	String(32) 	C380BEC2BFD727A4B6845133519F3AD6 	随机字符串，不长于32位。推荐随机数生成算法
		签名 	sign 	是 	String(32) 	5K8264ILTKCH16CQ2502SI8ZNMTM67VS 	通过签名算法计算得出的签名值，详见签名生成算法
		签名类型 	sign_type 	否 	String(32) 	HMAC-SHA256 	签名类型，目前支持HMAC-SHA256和MD5，默认为MD5*/
        parameters.put("appid", pm.getTerminalNo());
        parameters.put("mch_id", pm.getMerchantNo());
        parameters.put("nonce_str", randomStr);
        if (StringUtils.isNotBlank(order.getTransactionId())) {
            parameters.put("transaction_id", order.getTransactionId()); // 订单id
        } else {
            parameters.put("out_trade_no", order.getTransBillNo()); // 订单id
        }

        // 设置签名
        String sign = PayCommonUtil.getSign(parameters, pm.getKey());
        parameters.put("sign", sign);
        // 封装请求参数结束
        String requestXML = PayCommonUtil.getRequestXml(parameters);
        // 调用查询订单接口
        String result = HttpUtil.Post("https://api.mch.weixin.qq.com/pay/orderquery", requestXML).getHtml();
        PayLog.getLogger().info("微信查询订单响应报文：" + result);
        PayOrderQueryData data = new PayOrderQueryData();
        @SuppressWarnings("unchecked")
        Map<String, String> map = XMLUtil.doXMLParse(result);
        if (map.get("return_code").equals("FAIL")) {
            String returnMsg = map.get("return_msg");
            returnMsg = StringUtils.isBlank(returnMsg) ? "签名失败" : returnMsg;
            PayLog.getLogger().error("[微信:{},{}]查询订单失败.", order.getTransBillNo(), order.getConnBillno());
            data.setCode("-1");
            data.setRespMsg("支付失败");
            return data;
        }
        if (map.get("result_code").equals("FAIL")) {
            String err_code_des = map.get("err_code_des");
            //return JSON.toJSONString(ResultDTO.error(Constants.PAY_FAIL, err_code_des));
            PayLog.getLogger().error("[微信:{},{}]查询订单失败.", order.getTransBillNo(), order.getConnBillno());
            data.setCode("-1");
            data.setRespMsg("支付失败");
            if (order.getOrderStatus().equals(PayOrder.OrderStatus.支付成功) == false
                    && order.getOrderStatus().equals(PayOrder.OrderStatus.支付失败) == false) {
                order.setOrderStatus(PayOrder.OrderStatus.支付失败);
                order.setOrderStatusDesc(err_code_des);
            }
            return data;
        }
        PayLog.getLogger().info("[微信:{},{}]查询订单成功.", order.getTransBillNo(), order.getConnBillno());
        data.setCode("0");
        data.setAmount(order.getFactAmount());
        data.setTradeTime(order.getCreatedDate());
        data.setRespMsg("支付成功");
        if (order.getOrderStatus().equals(PayOrder.OrderStatus.支付成功) == false) {
            // 检查回调信息
            order.setOrderStatus(PayOrder.OrderStatus.支付成功);
            order.setOrderStatusDesc(DateUtil.getNow() + ":" + PayOrder.OrderStatus.支付成功.getName());
        }
        return data;
    }

}
