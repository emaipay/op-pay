package com.hitler.payservice;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.alibaba.fastjson.JSON;
import com.hitler.core.exception.ThirdPayBussinessException;
import com.hitler.core.log.PayLog;
import com.hitler.core.utils.DateUtil;
import com.hitler.core.utils.HttpUtils;
import com.hitler.core.utils.StringUtils;
import com.hitler.core.utils.XmlUtil;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayOrder;
import com.hitler.entity.PayPlatform;
import com.hitler.payservice.platform.mf.KeyValue;
import com.hitler.payservice.platform.mf.KeyValues;
import com.hitler.payservice.platform.mf.MfConstants;
import com.hitler.payservice.support.IpayService;
import com.hitler.payservice.support.PayOrderQueryData;

public class MfPayService implements IpayService {

	private static String CHARSET = "UTF-8";
	// 付款回调页面地址
	public static final String CALLBACK_PAGE_PATH = "/mf/callback-page";
	public static final String CALLBACK_DATA_PATH = "/mf/callback-data";

	public static final String QUERY_ORDER_URL = "http://pay.miaofupay.com/query";

	@Override
	public Map<String, Object> getPayData(HttpServletRequest request, PayOrder order, String payCode) {
		PayMerchant pm = order.getMerchantId();
		PayPlatform pp = order.getPlatformId();
		Map<String, Object> map = new HashMap<String, Object>();
		String call = request.getScheme() + "://" + request.getServerName();
		String referer = request.getScheme() + "://" + request.getServerName();
		String ocdataPath = call + "/pay/callback" + CALLBACK_DATA_PATH;
		String ocpagePath = call + "/pay/callback" + CALLBACK_PAGE_PATH;
		String currentDate = DateUtil.getNow();
		String customerIp = HttpUtils.getAddr(request);
		KeyValues kvs = new KeyValues();
		kvs.add(new KeyValue(MfConstants.INPUT_CHARSET, CHARSET));
		kvs.add(new KeyValue(MfConstants.NOTIFY_URL, ocdataPath));
		kvs.add(new KeyValue(MfConstants.RETURN_URL, ocpagePath));
		kvs.add(new KeyValue(MfConstants.PAY_TYPE, "1"));
		kvs.add(new KeyValue(MfConstants.BANK_CODE, payCode));
		kvs.add(new KeyValue(MfConstants.MERCHANT_CODE, pm.getMerchantNo()));
		kvs.add(new KeyValue(MfConstants.ORDER_NO, order.getBillNo()));
		kvs.add(new KeyValue(MfConstants.ORDER_AMOUNT, order.getOrderAmount() + ""));
		kvs.add(new KeyValue(MfConstants.ORDER_TIME, currentDate));
		kvs.add(new KeyValue(MfConstants.REQ_REFERER, referer));
		kvs.add(new KeyValue(MfConstants.CUSTOMER_IP, customerIp));
		kvs.add(new KeyValue(MfConstants.RETURN_PARAMS, order.getBillNo().substring(4)));
		String sign = kvs.sign(pm.getKey(), CHARSET);
		map.put(MfConstants.INPUT_CHARSET, CHARSET);
		map.put(MfConstants.NOTIFY_URL, ocdataPath);
		map.put(MfConstants.RETURN_URL, ocpagePath);
		map.put(MfConstants.PAY_TYPE, "1");
		map.put(MfConstants.BANK_CODE, payCode);
		map.put(MfConstants.MERCHANT_CODE, pm.getMerchantNo());
		map.put(MfConstants.ORDER_NO, order.getBillNo());
		map.put(MfConstants.ORDER_AMOUNT, order.getOrderAmount() + "");
		map.put(MfConstants.ORDER_TIME, currentDate);
		map.put(MfConstants.REQ_REFERER, referer);
		map.put(MfConstants.CUSTOMER_IP, customerIp);
		map.put(MfConstants.RETURN_PARAMS, order.getBillNo().substring(4));
		map.put(MfConstants.SIGN, sign);
		PayLog.getLogger().info("[秒付:{},{}]表单数据:{}", order.getTransBillNo(), order.getConnBillno(),
				JSON.toJSONString(map));
		map.put("postUrl", pp.getPayUrl());
		return map;
	}

	@Override
	public String getPayOrderNo(HttpServletRequest request) {
		PayLog.getLogger().info("[秒付:{}]返回订单号", request.getParameter("return_params"));
		return request.getParameter("return_params");
	}

	@Override
	public void checkPayBackData(HttpServletRequest request, PayOrder order) throws Exception {
		PayLog.getLogger().info("[秒付:{},{}]开始校验数据", order.getTransBillNo(), order.getConnBillno());
		PayMerchant pm = order.getMerchantId();
		String tradeStatus = request.getParameter("trade_status");
		String orderAmount = request.getParameter("order_amount");
		if (validPageNotify(request, pm.getKey(), order.getTransBillNo(), order.getConnBillno())) {
			if (!"success".equalsIgnoreCase(tradeStatus)) {
				throw new ThirdPayBussinessException("支付失败");
			}
			String merchantCode = request.getParameter("merchant_code");
			if (StringUtils.equalsIgnoreCase(pm.getMerchantNo(), merchantCode) == false) {
				throw new ThirdPayBussinessException("商户号不一致" + pm.getMerchantNo() + ":" + merchantCode);
			}
			double realFactMoney = Double.parseDouble(orderAmount);
			if (order.getOrderAmount() != realFactMoney) {
				throw new ThirdPayBussinessException(
						"实际成交金额与您提交的订单金额不一致:" + order.getOrderAmount() + ":" + realFactMoney);
			}
			order.setFactAmount(order.getOrderAmount());
			PayLog.getLogger().info("[秒付:{},{}]校验数据成功", order.getTransBillNo(), order.getConnBillno());
		} else {
			throw new ThirdPayBussinessException("非法签名");
		}
	}

	private boolean validPageNotify(HttpServletRequest req, String merKey, String orderId, String connOrderId) {
		String merchantCode = req.getParameter("merchant_code");
		String notifyType = req.getParameter("notify_type");
		String orderNo = req.getParameter("order_no");
		String orderAmount = req.getParameter("order_amount");
		String orderTime = req.getParameter("order_time");
		String returnParams = req.getParameter("return_params");
		String tradeNo = req.getParameter("trade_no");
		String tradeTime = req.getParameter("trade_time");
		String tradeStatus = req.getParameter("trade_status");
		String sign = req.getParameter("sign");
		KeyValues kvs = new KeyValues();

		kvs.add(new KeyValue("merchant_code", merchantCode));
		kvs.add(new KeyValue("notify_type", notifyType));
		kvs.add(new KeyValue("order_no", orderNo));
		kvs.add(new KeyValue("order_amount", orderAmount));
		kvs.add(new KeyValue("order_time", orderTime));
		kvs.add(new KeyValue("return_params", returnParams));
		kvs.add(new KeyValue("trade_no", tradeNo));
		kvs.add(new KeyValue("trade_time", tradeTime));
		kvs.add(new KeyValue("trade_status", tradeStatus));
		String thizSign = kvs.sign(merKey, CHARSET);
		PayLog.getLogger().info("[秒付:{},{}]检查秒付数据:{}", orderId, connOrderId, JSON.toJSONString(kvs));
		if (thizSign.equalsIgnoreCase(sign)) {
			return true;
		} else {
			PayLog.getLogger().info("[秒付:{},{}]数据失败:{}", orderId, connOrderId, JSON.toJSONString(kvs));
		}
		return false;
	}

	/**
	 * <pay> <response> <is_success>TRUE</is_success>
	 * <merchant_code>17570882</merchant_code>
	 * <order_no>PR161208094940589537</order_no> <order_time>2016-12-08
	 * 09:49:40</order_time> <order_amount>0.01</order_amount>
	 * <trade_no>3063648409780441</trade_no> <trade_time>2016-12-08
	 * 09:49:40</trade_time> <trade_status>success</trade_status>
	 * <sign>5f0455ed1484343dc32e0ebe7c2e0f34</sign> </response> </pay>
	 */
	@Override
	public PayOrderQueryData queryOrder(HttpServletRequest request,PayOrder order) throws Exception {
		PayLog.getLogger().info("[秒付:{},{}]开始查询订单",order.getTransBillNo(),order.getConnBillno());
		PayMerchant pm = order.getMerchantId();
		KeyValues kvs = new KeyValues();
		kvs.add(new KeyValue(MfConstants.INPUT_CHARSET, CHARSET));
		kvs.add(new KeyValue(MfConstants.MERCHANT_CODE, pm.getMerchantNo()));
		kvs.add(new KeyValue(MfConstants.ORDER_NO, order.getBillNo()));
		String sign = kvs.sign(pm.getKey(), CHARSET);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(MfConstants.INPUT_CHARSET, CHARSET);
		paramMap.put(MfConstants.MERCHANT_CODE, pm.getMerchantNo());
		paramMap.put(MfConstants.ORDER_NO, order.getBillNo());
		paramMap.put(MfConstants.SIGN, sign);
		String resp = HttpUtils.sendGet(QUERY_ORDER_URL, paramMap);
		PayLog.getLogger().info("[秒付:{},{}]返回结果:{}",order.getTransBillNo(),order.getConnBillno(),resp);
		PayOrderQueryData data = new PayOrderQueryData();
		data.setCode("-1");
		try {
			Element ele = XmlUtil.xml2Ele(resp);
			if (!"pay".equals(ele.getName())) {
				data.setRespMsg("查询订单失败:返回报文错误!");
				return data;
			}
			Element eleResp = ele.element("response");// 拿到pay节点
			if (!verifyQuery(eleResp, pm.getKey())) {
				data.setRespMsg("查询订单失败:签名不一致!");
				return data;
			}
			String isSuccess = eleResp.element("is_success").getText();
			if ("true".equalsIgnoreCase(isSuccess)) {
				String orderAmount = eleResp.element("order_amount").getText();
				String merNoResp = eleResp.element("merchant_code").getText();
				String tradeStatus = eleResp.element("trade_status").getText();
				String tradeTime = eleResp.element("trade_time").getText();
				String orderNo = eleResp.element("order_no").getText();
				if (order.getOrderAmount() != Double.parseDouble(orderAmount)) {
					data.setRespMsg("查询订单失败:秒付返回订单金额与平台保存金额不一致,请商户后台确认充值金额!");
					return data;
				}
				if (!pm.getMerchantNo().equals(merNoResp)) {
					data.setRespMsg("查询订单失败:商户号不一致!");
					return data;
				}
				if (!order.getBillNo().equals(orderNo)) {
					data.setRespMsg("查询订单失败:订单号不一致!");
					return data;
				}
				if (order.getOrderAmount() != Double.parseDouble(orderAmount)) {
					data.setRespMsg("查询订单失败:秒付返回订单金额与平台保存金额不一致,请商户后台确认充值金额!");
					return data;
				}
				if ("failed".equals(tradeStatus)) {
					data.setRespMsg("查询订单失败:秒付返回failed,请查询秒付商户后台该笔是否支付成功!");
					return data;
				} else if ("paying".equals(tradeStatus)) {
					data.setRespMsg("查询订单失败:秒付返回paying,请查询秒付商户后台该笔是否支付成功!");
					return data;
				}
				data.setAmount(Double.parseDouble(orderAmount));
				data.setTradeTime(DateUtil.str2Date(tradeTime));
				PayLog.getLogger().info("[秒付:{},{}]查询订单成功",order.getTransBillNo(),order.getConnBillno());
			} else {
				data.setRespMsg("查询订单失败:" + eleResp.element("errror_msg").getText());
				return data;
			}

		} catch (DocumentException e) {
			PayLog.getLogger().info("[秒付:{},{}]异常",order.getTransBillNo(),order.getConnBillno(),e);
			data.setRespMsg("查询订单失败,读取返回报文失败!");
		}
		data.setCode("0");
		return data;
	}

	public boolean verifyQuery(Element eleResp, String merKey) {
		String isSuccess = eleResp.element("is_success").getText();
		String orderTime = eleResp.element("order_time").getText();
		String sign = eleResp.element("sign").getText();
		String orderAmount = eleResp.element("order_amount").getText();
		String merNoResp = eleResp.element("merchant_code").getText();
		String tradeStatus = eleResp.element("trade_status").getText();
		String tradeTime = eleResp.element("trade_time").getText();
		String tradeNo = eleResp.element("trade_no").getText();
		String orderNo = eleResp.element("order_no").getText();
		KeyValues kvs = new KeyValues();
		kvs.add(new KeyValue("is_success", isSuccess));
		kvs.add(new KeyValue("order_time", orderTime));
		kvs.add(new KeyValue("order_amount", orderAmount));
		kvs.add(new KeyValue("merchant_code", merNoResp));
		kvs.add(new KeyValue("trade_status", tradeStatus));
		kvs.add(new KeyValue("trade_time", tradeTime));
		kvs.add(new KeyValue("trade_no", tradeNo));
		kvs.add(new KeyValue("order_no", orderNo));
		String signs = kvs.sign(merKey, CHARSET);
		if (signs.equals(sign)) {
			return true;
		}
		return false;

	}

}
