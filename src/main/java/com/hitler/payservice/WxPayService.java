package com.hitler.payservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

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
import com.hitler.util.HttpReqUtil;
import com.hitler.util.HttpUtil;
import com.hitler.util.JUUIDUtil;
import com.hitler.util.PayCommonUtil;
import com.hitler.util.XMLUtil;

public class WxPayService implements IpayService {
	
	public static final String CALLBACK_PAGE_PATH = "/wx/callback-page";
	public static final String CALLBACK_DATA_PATH = "/wx/callback-data";

	@Override
	public Map<String, Object> getPayData(HttpServletRequest request,
			PayOrder order, String payCode) {
		Map<String, Object> respMap = new HashMap<String, Object>();
		if(payCode.equals("APP")){
			respMap.put("redirect", 0);
			String params = appPayHandle(request, order, payCode);
			respMap.put("postUrl", params);
		}else{
			ResultDTO<SortedMap<String, String>> dto = wapPayHandle(request, order, payCode);
			respMap.put("redirect", 2);
			respMap.put("postUrl", dto);
		}
		return respMap;
	}
	
	private String getPayResult(HttpServletRequest request, PayOrder order, String payCode, PayMerchant pm, String randomStr) throws Exception{
		//String call = request.getScheme() + "://" + request.getServerName();
		String call = "http://paytool.mzball.com";
		String notifyUrl = call + "/pay/callback" + CALLBACK_DATA_PATH;
		PayLog.getLogger().info("notifyUrl:" + notifyUrl);
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", pm.getTerminalNo());
		parameters.put("mch_id", pm.getMerchantNo());
		parameters.put("nonce_str", randomStr);
		parameters.put("body", "魔智猎球商城充值");
		parameters.put("out_trade_no", order.getTransBillNo()); // 订单id
		// parameters.put("attach", "test");
		parameters.put("total_fee", Math.round(order.getOrderAmount() * 100)+ "");
		String ip = com.hitler.util.RequestUtils.getIpAddr(request);
		parameters.put("spbill_create_ip", ip);
		parameters.put("notify_url", notifyUrl);
		parameters.put("trade_type", payCode);
		if(payCode.equals("JSAPI")){//公众号支付
			parameters.put("openid", order.getOpenid());
		}
		
		// 设置签名
		//String sign = PayCommonUtil.getSign(parameters, pm.getKey());
		String sign = PayCommonUtil.generateSignature(parameters, pm.getKey());
		parameters.put("sign", sign);
		// 封装请求参数结束
		String requestXML = PayCommonUtil.getRequestXml(parameters);
		// 调用统一下单接口
		PayLog.getLogger().info("微信支付"+payCode+"请求报文：" + requestXML);
		String result = HttpUtil.Post("https://api.mch.weixin.qq.com/pay/unifiedorder", requestXML).getHtml();
		PayLog.getLogger().info("微信支付"+payCode+"响应报文：" + result);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private ResultDTO<SortedMap<String, String>> wapPayHandle(HttpServletRequest request, PayOrder order, String payCode){
		if(StringUtils.isBlank(order.getOpenid())){
			return ResultDTO.error(Constants.PAY_FAIL, "公众号支付openid不能为空");
		}
		PayMerchant pm = order.getMerchantId();
		String randomStr = PayCommonUtil.generateUUID();
		
		try {
			String result = getPayResult(request, order, payCode, pm, randomStr);
			Map<String, String> map = XMLUtil.doXMLParse(result);
			if(map.get("return_code").equals("FAIL")){
				String returnMsg = map.get("return_msg");
				return ResultDTO.error(Constants.PAY_FAIL, StringUtils.isBlank(returnMsg)?"签名失败":returnMsg);
			}
			if(map.get("result_code").equals("FAIL")){
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
			parameterMap2.put("package", "prepay_id="+map.get("prepay_id"));
			parameterMap2.put("nonceStr", randomStr);
			// 本来生成的时间戳是13位，但是ios必须是10位，所以截取了一下
			parameterMap2.put("timeStamp",(System.currentTimeMillis()/1000)+"");
			parameterMap2.put("signType", "MD5");
			//String sign2 = PayCommonUtil.getSign(parameterMap2, pm.getKey());
			String sign2 = PayCommonUtil.generateSignature(parameterMap2, pm.getKey());
			parameterMap2.put("paySign", sign2);

			return ResultDTO.success(parameterMap2, "成功");
		} catch (Exception e) {
			PayLog.getLogger().error("appPayHandle Exception:" + e.getMessage(), e);
			return ResultDTO.error(Constants.PAY_FAIL, e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private String appPayHandle(HttpServletRequest request, PayOrder order, String payCode){
		PayMerchant pm = order.getMerchantId();
		String randomStr = PayCommonUtil.generateUUID();
	
		try {
			String result = getPayResult(request, order, payCode, pm, randomStr);
			Map<String, String> map = XMLUtil.doXMLParse(result);
			if(map.get("return_code").equals("FAIL")){
				String returnMsg = map.get("return_msg");
				return JSON.toJSONString(ResultDTO.error(Constants.PAY_FAIL, StringUtils.isBlank(returnMsg)?"签名失败":returnMsg));
			}
			if(map.get("result_code").equals("FAIL")){
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
			Integer timeStamp = (int) (System.currentTimeMillis()/1000);
			parameterMap2.put("timestamp",timeStamp+"");
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
			PayLog.getLogger().info("getPayOrderNo微信支付系统发送的数据"+notifyXml);
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
		if(StringUtils.isNotBlank(order.getTransactionId())){
			parameters.put("transaction_id", order.getTransactionId()); // 订单id
		}else{
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
		if(map.get("return_code").equals("FAIL")){
			String returnMsg = map.get("return_msg");
			returnMsg = StringUtils.isBlank(returnMsg)?"签名失败":returnMsg;
			PayLog.getLogger().error("[微信:{},{}]查询订单失败.", order.getTransBillNo(), order.getConnBillno());
			data.setCode("-1");
			data.setRespMsg("支付失败");
			return data;
		}
		if(map.get("result_code").equals("FAIL")){
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
