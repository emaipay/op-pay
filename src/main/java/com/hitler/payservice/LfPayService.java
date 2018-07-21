package com.hitler.payservice;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hitler.core.exception.ThirdPayBussinessException;
import com.hitler.core.log.PayLog;
import com.hitler.core.utils.HttpUtils;
import com.hitler.core.utils.StringUtils;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayOrder;
import com.hitler.entity.PayPlatform;
import com.hitler.payservice.platform.mf.MD5Encoder;
import com.hitler.payservice.support.IpayService;
import com.hitler.payservice.support.PayOrderQueryData;

/**
 * 乐付
 * 
 * @author x
 *
 */
@SuppressWarnings("all")
public class LfPayService implements IpayService {

	public static final String CALLBACK_PAGE_PATH = "/lf/callback-page";
	public static final String CALLBACK_DATA_PATH = "/lf/callback-data";
	// public static final String mobile_PAGEBACK =
	// "http://183.238.56.249:8019/oc/sider/callback/lf/callback-data";

	@Override
	public Map<String, Object> getPayData(HttpServletRequest request, PayOrder order, String payCode) {
		try {
			PayMerchant pm=order.getMerchantId();
			PayPlatform pp=order.getPlatformId();
			String call = request.getScheme() + "://" + request.getServerName();
			String callbackData = call + CALLBACK_DATA_PATH;
			String callbackPage = call + CALLBACK_PAGE_PATH;
			String createDate = new SimpleDateFormat("yyyyMMddHHmmss").format(order.getCreatedDate());

			if (!"wx_pay".equals(payCode) && !"ali_pay".equals(payCode)) {
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				String good_name = "recharge";
				String goods_detail = "pay";
				// String[] getUrl = payUrl.split("|");
				String customerIp = HttpUtils.getAddr(request);
				String md5ForPay = "partner=" + pm.getMerchantNo() + "&service=speedy_pay&out_trade_no=" + order.getTransBillNo()
						+ "&amount_str=" + order.getOrderAmount() + "&tran_ip=" + customerIp + "&good_name=" + good_name
						+ "&request_time=" + createDate + "&return_url=" + callbackData + "&verfication_code=" + pm.getKey();
				String sigure = MD5Encoder.encode(md5ForPay);
				paramsMap.put("service", "speedy_pay");
				paramsMap.put("partner", pm.getMerchantNo());
				paramsMap.put("input_charset", "UTF-8");
				paramsMap.put("sign_type", "MD5");
				paramsMap.put("sign", sigure);
				paramsMap.put("request_time", createDate);
				paramsMap.put("return_url", callbackData);
				paramsMap.put("out_trade_no", order.getTransBillNo());
				paramsMap.put("amount_str", order.getOrderAmount() + "");
				paramsMap.put("tran_ip", customerIp);
				paramsMap.put("good_name", good_name);
				paramsMap.put("goods_detail", goods_detail);
				paramsMap.put("bank_sn", payCode);
				paramsMap.put("advice_url", callbackPage);
				paramsMap.put("postUrl", pp.getPayUrl());
				PayLog.getLogger().info("[乐付:{}请求:{}]请求参数:{}", payCode, order.getTransBillNo(), JSON.toJSONString(paramsMap));
				return paramsMap;
			} else if ("wx_pay".equals(payCode)) {
				Map<String, String> paramsMap = new HashMap<String, String>();
				String wx_pay_type = "wx_sm";
				String subject = "recharge";
				String sub_body = "pay";
				// String[] getUrl = payUrl.split("|");
				String md5ForPay = "amount_str=" + order.getOrderAmount() + "&out_trade_no=" + order.getTransBillNo()
						+ "&return_url=" + callbackData + "&sub_body=" + sub_body + "&subject=" + subject
						+ "&wx_pay_type=" + wx_pay_type + "&" + pm.getKey();

				String content = URLEncoder.encode(md5ForPay, "UTF-8");
				String sigure = MD5Encoder.encode(md5ForPay);
				paramsMap.put("service", payCode);
				paramsMap.put("partner", pm.getMerchantNo());
				paramsMap.put("input_charset", "UTF-8");
				paramsMap.put("sign_type", "MD5");
				paramsMap.put("sign", sigure);
				paramsMap.put("request_time", createDate);
				paramsMap.put("content", content);
				PayLog.getLogger().info("[乐付:{}请求:{}]请求参数:{}", payCode, order.getTransBillNo(), JSON.toJSONString(paramsMap));
				String resp = HttpUtils.sendPost(pp.getPayUrl(), paramsMap);
				PayLog.getLogger().info("[乐付:{}请求:{}]请求结果:{}", payCode, order.getTransBillNo(), resp);

				String smUrl = "";
				JSONObject rspJson = JSONObject.parseObject(resp);
				if (rspJson.getString("is_succ").equals("T")) {
					String result = rspJson.getString("result_json");
					JSONObject result_json = JSONObject.parseObject(result);
					smUrl = result_json.getString("wx_pay_sm_url");
					Map<String, Object> requestMap = new HashMap<String, Object>();
					requestMap.put("postUrl", smUrl);
					requestMap.put("qrCode", true);
					return requestMap;
				}else{
					String msg = rspJson.get("result_msg") != null ? rspJson.get("result_msg").toString() : "";
					String reson = rspJson.get("fail_reason") != null ? rspJson.get("fail_reason").toString() : "";
					Map<String, Object> requestMap = new HashMap<String, Object>();
					requestMap.put("error", msg + ":" + reson);
					requestMap.put("postUrl", "");
					return requestMap;
				}
			} else {
				Map<String, String> paramsMap = new HashMap<String, String>();
				String wx_pay_type = "ali_sm";
				String subject = "recharge";
				String sub_body = "pay";
				// String[] getUrl = payUrl.split("|");
				String md5ForPay = "ali_pay_type=" + wx_pay_type + "&amount_str=" + order.getOrderAmount() + "&out_trade_no=" + order.getTransBillNo()
						+ "&return_url=" + callbackData + "&sub_body=" + sub_body + "&subject=" + subject
						+ "&" + pm.getKey();

				String content = URLEncoder.encode(md5ForPay, "UTF-8");
				String sigure = MD5Encoder.encode(md5ForPay);
				paramsMap.put("service", payCode);
				paramsMap.put("partner", pm.getMerchantNo());
				paramsMap.put("input_charset", "UTF-8");
				paramsMap.put("sign_type", "MD5");
				paramsMap.put("sign", sigure);
				paramsMap.put("request_time", createDate);
				paramsMap.put("content", content);
				PayLog.getLogger().info("[乐付:{}请求:{}]请求参数:{}", payCode, order.getTransBillNo(), JSON.toJSONString(paramsMap));
				String resp = HttpUtils.sendPost(pp.getPayUrl(), paramsMap);
				PayLog.getLogger().info("[乐付:{}请求:{}]请求结果:{}", payCode, order.getTransBillNo(), resp);
				String smUrl = "";
				JSONObject rspJson = JSONObject.parseObject(resp);
				if (rspJson.getString("is_succ").equals("T")) {
					String result = rspJson.getString("result_json");
					JSONObject result_json = JSONObject.parseObject(result);
					smUrl = result_json.getString("ali_pay_sm_url");
					Map<String, Object> requestMap = new HashMap<String, Object>();
					requestMap.put("postUrl", smUrl);
					requestMap.put("qrCode", true);
					return requestMap;
				}else{
					String msg = rspJson.get("result_msg") != null ? rspJson.get("result_msg").toString() : "";
					String reson = rspJson.get("fail_reason") != null ? rspJson.get("fail_reason").toString() : "";
					Map<String, Object> requestMap = new HashMap<String, Object>();
					requestMap.put("error", msg + ":" + reson);
					requestMap.put("postUrl", "");
					return requestMap;
				}
			}
		} catch (Exception e) {
			PayLog.getLogger().error("[乐付组装参数失败:{}]", order.getTransBillNo(), e);
		}
		return null;
	}

	@Override
	public String getPayOrderNo(HttpServletRequest request) {
		String content = request.getParameter("content");
		if (StringUtils.isEmpty(content)) {
			return null;
		}
		String outTradeNo = getParam(content, "out_trade_no");
		if (StringUtils.isNotEmpty(outTradeNo)) {
			return outTradeNo;
		}

		return null;// 商户平台订单号
	}

	private String getParam(String content, String filed) {
		String[] params = content.split("&");
		for (int i = 0; i < params.length; i++) {
			String[] param = params[i].split("=");
			if (param[0].equals(filed)) {
				return param[1];
			}
		}
		return null;
	}

	@Override
	public void checkPayBackData(HttpServletRequest request, PayOrder order) throws Exception {
		PayMerchant pm = order.getMerchantId();
		String content = request.getParameter("content");
		String sign = request.getParameter("sign");
		if (StringUtils.isEmpty(sign) || StringUtils.isEmpty(content)) {
			throw new ThirdPayBussinessException("回调参数缺失");
		}
		content += "&verfication_code=" + pm.getKey();
		String sigure = MD5Encoder.encode(content);
		if (!sign.equals(sigure)) {
			throw new ThirdPayBussinessException("乐付支付回调,数据来源校验失败");
		}
		String status = getParam(content, "status");
		if (!"1".equals(status)) {
			if ("0".equals(status)) {
				throw new ThirdPayBussinessException("订单处理中");
			} else if ("2".equals(status)) {
				throw new ThirdPayBussinessException("订单处理失败");
			} else {
				throw new ThirdPayBussinessException("未知错误:" + status);
			}
		}
		String amountStr = getParam(content, "amount_str");
		order.setFactAmount(Double.parseDouble(amountStr));
	}

	@Override
	public PayOrderQueryData queryOrder(HttpServletRequest request, PayOrder order) throws Exception {
		PayOrderQueryData data=new PayOrderQueryData();
		try {
			PayMerchant pm=order.getMerchantId();
			PayPlatform pp=order.getPlatformId();
			String md5ForPay = "out_trade_no=" +order.getTransBillNo()  + "&"+ pm.getKey();
			String createDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String content = URLEncoder.encode(md5ForPay, "utf-8");
			String sigure = MD5Encoder.encode(md5ForPay);
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("service", "find_trade");
			paramsMap.put("partner", pm.getMerchantNo());
			paramsMap.put("input_charset", "UTF-8");
			paramsMap.put("sign_type", "MD5");
			paramsMap.put("sign", sigure);
			paramsMap.put("request_time", createDate);
			paramsMap.put("content", content);
			String resp=HttpUtils.sendPost(pp.getPayUrl(), paramsMap);
			JSONObject rspJson = JSONObject.parseObject(resp);
			if (rspJson.getString("is_succ").equals("T")) {
				String result = rspJson.getString("result_json");
				JSONObject resultJson = JSONObject.parseObject(result);
				String status=resultJson.getString("status");
				if(!"1".equals(status)){
					if("0".equals(status)){
						data.setRespMsg("订单处理中");
						return data;
					}else if("2".equals(status)){
						data.setRespMsg("订单处理失败");
						return data;
					}else{
						data.setRespMsg("未知错误:"+status);
						return data;
					}
				}
				String outTradeNo=resultJson.getString("out_trade_no");
				if(!order.getTransBillNo().equals(outTradeNo)){
					data.setRespMsg("订单号不一致:"+outTradeNo);
					return data;
				}
				double amount=Double.parseDouble(resultJson.getString("amount_str"));
				if(amount!=order.getOrderAmount()){
					data.setRespMsg("订单金额不一致:"+amount+":"+order.getOrderAmount());
					return data;
				}
				data.setTradeTime(new Date());
				data.setAmount(amount);
				return data;
			}else {
				data.setRespMsg("查询订单失败:"+rspJson.getString("result_msg")+":"+rspJson.getString("fail_reason"));
				return data;
			}
		} catch (Exception e) {
			PayLog.getLogger().error("[乐付查询订单失败:{}]", order.getTransBillNo(), e);
			data.setRespMsg("查询订单失败"+e.getMessage());
			return data;
		}
	}

}
