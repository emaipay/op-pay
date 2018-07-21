package com.hitler.payservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import com.alibaba.fastjson.JSON;
import com.hitler.core.exception.ThirdPayBussinessException;
import com.hitler.core.log.PayLog;
import com.hitler.core.utils.ObjectUtils;
import com.hitler.core.utils.StringUtils;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayOrder;
import com.hitler.entity.PayPlatform;
import com.hitler.payservice.platform.yfb.YfbAppConstants;
import com.hitler.payservice.platform.yfb.YfbDateUtils;
import com.hitler.payservice.platform.yfb.YfbHttpUtils;
import com.hitler.payservice.platform.yfb.YfbKeyValue;
import com.hitler.payservice.platform.yfb.YfbKeyValues;
import com.hitler.payservice.support.IpayService;
import com.hitler.payservice.support.PayOrderQueryData;
@SuppressWarnings("all")
public class YfbPayService implements IpayService {

	public static final String CHARSET = "UTF-8"; // 这里填写当前系统字符集编码，取值UTF-8或者GBK
	private static final String PAY_TYPE = "1"; // 支付方式，目前暂只支持网银支付，取值为1
	public static final String RETURN_PARAMS = "JT-YFB";

	// 付款回调页面地址
	public static final String CALLBACK_PAGE_PATH = "/yfb/callback-page";

	// 付款回调数据地址
	public static final String CALLBACK_DATA_PATH = "/yfb/callback";

	@Override
	public Map<String, Object> getPayData(HttpServletRequest request, PayOrder order, String payCode) {
		PayMerchant pm = order.getMerchantId();
		PayPlatform pp = order.getPlatformId();
		String orderNo = order.getTransBillNo();
		Double orderAmount = order.getOrderAmount();
		// 当前请求路径
		String reqPath = request.getScheme() + "://" + request.getServerName();
		String callbackUrl = reqPath + "/pay/callback" + CALLBACK_PAGE_PATH;
		String notifyUrl = reqPath + "/pay/callback" + CALLBACK_DATA_PATH;
		String customerIp = YfbHttpUtils.getAddr(request);
		Map<String, Object> map = new HashMap<String, Object>();
		// 易付宝的实现
		String currentDate = YfbDateUtils.format(order.getCreatedDate());
		String sign = makePaySign(notifyUrl, callbackUrl, payCode, pm.getMerchantNo(), orderNo, orderAmount, reqPath,
				customerIp, pm.getKey(), order.getCreatedDate());
		map.put(YfbAppConstants.INPUT_CHARSET, CHARSET);
		map.put(YfbAppConstants.NOTIFY_URL, notifyUrl);
		map.put(YfbAppConstants.RETURN_URL, callbackUrl);
		map.put(YfbAppConstants.PAY_TYPE, PAY_TYPE);
		map.put(YfbAppConstants.BANK_CODE, payCode);
		map.put(YfbAppConstants.MERCHANT_CODE, pm.getMerchantNo());
		map.put(YfbAppConstants.ORDER_NO, orderNo);
		map.put(YfbAppConstants.ORDER_AMOUNT, String.valueOf(orderAmount));
		map.put(YfbAppConstants.ORDER_TIME, currentDate);
		map.put(YfbAppConstants.PRODUCT_NAME, "");
		map.put(YfbAppConstants.PRODUCT_NUM, "");
		map.put(YfbAppConstants.REQ_REFERER, reqPath);
		map.put(YfbAppConstants.CUSTOMER_IP, customerIp);
		map.put(YfbAppConstants.CUSTOMER_PHONE, "");
		map.put(YfbAppConstants.RECEIVE_ADDRESS, "");
		map.put(YfbAppConstants.RETURN_PARAMS, RETURN_PARAMS);
		map.put(YfbAppConstants.SIGN, sign);
		String postUrl = pp.getPayUrl() + "/gateway";
		map.put("postUrl", postUrl);
		PayLog.getLogger().info("[易付宝:{},{}]组装数据:{}", order.getTransBillNo(), order.getConnBillno(),
				JSON.toJSONString(map));
		return map;
	}

	private String makePaySign(String notifyUrl, String callbackUrl, String payBankId, String merNo, String orderNo,
			double orderAmount, String reqHost, String customerIp, String merKey, Date orderCreateDate) {
		YfbKeyValues kvs = new YfbKeyValues();
		kvs.add(new YfbKeyValue(YfbAppConstants.INPUT_CHARSET, CHARSET));
		kvs.add(new YfbKeyValue(YfbAppConstants.NOTIFY_URL, notifyUrl));
		kvs.add(new YfbKeyValue(YfbAppConstants.RETURN_URL, callbackUrl));
		kvs.add(new YfbKeyValue(YfbAppConstants.PAY_TYPE, PAY_TYPE));
		kvs.add(new YfbKeyValue(YfbAppConstants.BANK_CODE, payBankId));
		kvs.add(new YfbKeyValue(YfbAppConstants.MERCHANT_CODE, merNo));
		kvs.add(new YfbKeyValue(YfbAppConstants.ORDER_NO, orderNo));
		kvs.add(new YfbKeyValue(YfbAppConstants.ORDER_AMOUNT, String.valueOf(orderAmount)));

		String currentDate = YfbDateUtils.format(orderCreateDate);
		kvs.add(new YfbKeyValue(YfbAppConstants.ORDER_TIME, currentDate));
		kvs.add(new YfbKeyValue(YfbAppConstants.PRODUCT_NAME, ""));
		kvs.add(new YfbKeyValue(YfbAppConstants.PRODUCT_NUM, ""));
		kvs.add(new YfbKeyValue(YfbAppConstants.REQ_REFERER, reqHost));
		kvs.add(new YfbKeyValue(YfbAppConstants.CUSTOMER_IP, customerIp));
		kvs.add(new YfbKeyValue(YfbAppConstants.CUSTOMER_PHONE, ""));
		kvs.add(new YfbKeyValue(YfbAppConstants.RECEIVE_ADDRESS, ""));
		kvs.add(new YfbKeyValue(YfbAppConstants.RETURN_PARAMS, RETURN_PARAMS));
		String sign = kvs.sign(merKey, CHARSET);
		return sign;
	}

	@Override
	public String getPayOrderNo(HttpServletRequest request) {
		PayLog.getLogger().info("[易付宝:{}]充值回调订单号", request.getParameter(YfbAppConstants.ORDER_NO));
		return request.getParameter(YfbAppConstants.ORDER_NO);
	}

	@Override
	public void checkPayBackData(HttpServletRequest request, PayOrder order) throws Exception {
		PayLog.getLogger().info("[易付宝:{},{}]开始校验数据", order.getTransBillNo(), order.getConnBillno());
		PayMerchant pm = order.getMerchantId();
		YfbKeyValues kvs = new YfbKeyValues();
		kvs.add(new YfbKeyValue(YfbAppConstants.MERCHANT_CODE, request.getParameter(YfbAppConstants.MERCHANT_CODE)));
		kvs.add(new YfbKeyValue(YfbAppConstants.NOTIFY_TYPE, request.getParameter(YfbAppConstants.NOTIFY_TYPE)));
		kvs.add(new YfbKeyValue(YfbAppConstants.ORDER_NO, request.getParameter(YfbAppConstants.ORDER_NO)));
		kvs.add(new YfbKeyValue(YfbAppConstants.ORDER_AMOUNT, request.getParameter(YfbAppConstants.ORDER_AMOUNT)));
		kvs.add(new YfbKeyValue(YfbAppConstants.ORDER_TIME, request.getParameter(YfbAppConstants.ORDER_TIME)));
		kvs.add(new YfbKeyValue(YfbAppConstants.TRADE_NO, request.getParameter(YfbAppConstants.TRADE_NO)));
		kvs.add(new YfbKeyValue(YfbAppConstants.TRADE_TIME, request.getParameter(YfbAppConstants.TRADE_TIME)));
		kvs.add(new YfbKeyValue(YfbAppConstants.TRADE_STATUS, request.getParameter(YfbAppConstants.TRADE_STATUS)));
		kvs.add(new YfbKeyValue(YfbAppConstants.RETURN_PARAMS, request.getParameter(YfbAppConstants.RETURN_PARAMS)));
		String newSign = kvs.sign(pm.getKey(), CHARSET);
		String sign = request.getParameter(YfbAppConstants.SIGN);
		if (StringUtils.equalsIgnoreCase(newSign, sign) == false) {
			throw new ThirdPayBussinessException("非法签名");
		}
		double orderAmount = ObjectUtils.toDouble(request.getParameter(YfbAppConstants.ORDER_AMOUNT), 0);
		String tradeStatus = request.getParameter(YfbAppConstants.TRADE_STATUS);
		boolean success = "success".equals(tradeStatus);// 是否成功
		if (success == false) {
			throw new ThirdPayBussinessException("支付失败，易付宝返回状态：" + tradeStatus);
		}

		String merchantCode = request.getParameter(YfbAppConstants.MERCHANT_CODE);
		if (StringUtils.equalsIgnoreCase(pm.getMerchantNo(), merchantCode) == false) {
			throw new ThirdPayBussinessException("商户号不一致");
		}

		if (order.getOrderAmount().compareTo(orderAmount) != 0) {
			throw new ThirdPayBussinessException("实际成交金额与您提交的订单金额不一致");
		}
		order.setOrderStatusDesc("支付成功");
		order.setFactAmount(orderAmount);
		PayLog.getLogger().info("[易付宝:{},{}]校验数据成功", order.getTransBillNo(), order.getConnBillno());
	}

	@Override
	public PayOrderQueryData queryOrder(HttpServletRequest request, PayOrder order) throws Exception {
		PayLog.getLogger().info("[易付宝:{},{}]开始查询订单", order.getTransBillNo(), order.getConnBillno());
		PayMerchant pm = order.getMerchantId();
		PayPlatform pp = order.getPlatformId();
		String postUrl = pp.getPayUrl() + "/query";

		String orderNo = order.getTransBillNo();
		YfbKeyValues kvs = new YfbKeyValues();
		kvs.add(new YfbKeyValue(YfbAppConstants.INPUT_CHARSET, CHARSET));
		kvs.add(new YfbKeyValue(YfbAppConstants.MERCHANT_CODE, pm.getMerchantNo()));
		kvs.add(new YfbKeyValue(YfbAppConstants.ORDER_NO, orderNo));
		String sign = kvs.sign(pm.getKey(), CHARSET);

		Map<String, String> params = new HashMap<String, String>();
		params.put(YfbAppConstants.INPUT_CHARSET, CHARSET);
		params.put(YfbAppConstants.MERCHANT_CODE, pm.getMerchantNo());
		params.put(YfbAppConstants.ORDER_NO, orderNo);
		params.put(YfbAppConstants.SIGN, sign);

		String result = sendPost(postUrl, params);
		PayLog.getLogger().info("[易付宝:{},{}]返回结果:{}", order.getTransBillNo(), order.getConnBillno(), result);
		return xml2Response(result, pm.getKey(), order);
	}

	private PayOrderQueryData xml2Response(String xml, String merKey, PayOrder order)
			throws Exception {
		PayOrderQueryData data = new PayOrderQueryData();
		data.setCode("-1");
		Document document;
		try {
			document = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			throw new ThirdPayBussinessException("解析返回数据失败");
		}
		Node responseNode = document.getRootElement().selectSingleNode("response");
		String successText = responseNode.selectSingleNode("is_success").getText();
		boolean success = Boolean.valueOf(successText);
		if (success == false) {
			String errorMsg = responseNode.selectSingleNode("error_msg").getText();
			throw new ThirdPayBussinessException(errorMsg);
		}
		String sign = String.valueOf(responseNode.selectSingleNode(YfbAppConstants.SIGN).getText());
		Node tradeNode = responseNode.selectSingleNode("trade");
		if (tradeNode == null) {
			tradeNode = responseNode;
		}
		String merCode = tradeNode.selectSingleNode(YfbAppConstants.MERCHANT_CODE).getText();
		String orderNo = tradeNode.selectSingleNode(YfbAppConstants.ORDER_NO).getText();
		String orderAmount = tradeNode.selectSingleNode(YfbAppConstants.ORDER_AMOUNT).getText();
		String orderTime = tradeNode.selectSingleNode(YfbAppConstants.ORDER_TIME).getText();
		String tradeNo = tradeNode.selectSingleNode(YfbAppConstants.TRADE_NO).getText();
		String tradeTime = tradeNode.selectSingleNode(YfbAppConstants.TRADE_TIME).getText();
		String tradeStatus = tradeNode.selectSingleNode(YfbAppConstants.TRADE_STATUS).getText();
		YfbKeyValues kvs = new YfbKeyValues();
		kvs.add(new YfbKeyValue("is_success", successText));
		kvs.add(new YfbKeyValue(YfbAppConstants.MERCHANT_CODE, merCode));
		kvs.add(new YfbKeyValue(YfbAppConstants.ORDER_NO, orderNo));
		kvs.add(new YfbKeyValue(YfbAppConstants.ORDER_AMOUNT, orderAmount));
		kvs.add(new YfbKeyValue(YfbAppConstants.ORDER_TIME, orderTime));
		kvs.add(new YfbKeyValue(YfbAppConstants.TRADE_NO, tradeNo));
		kvs.add(new YfbKeyValue(YfbAppConstants.TRADE_TIME, tradeTime));
		kvs.add(new YfbKeyValue(YfbAppConstants.TRADE_STATUS, tradeStatus));
		String newSign = kvs.sign(merKey, CHARSET);
		if (sign.equals(newSign) == false) {
			data.setRespMsg("非法签名");
			return data;
		}
		if ("success".equals(tradeStatus) == false) {

			if ("failed".equals(tradeStatus)) {
				data.setRespMsg("订单查询失败");
				return data;
			} else if ("paying".equals(tradeStatus)) {
				data.setRespMsg("订单处理中");
				return data;
			}
			data.setRespMsg("未知异常");
			return data;
		}
		
		data.setAmount(ObjectUtils.toDouble(orderAmount));
		data.setTradeTime(ObjectUtils.toDate(tradeTime));
		PayLog.getLogger().info("[易付宝:{},{}]查询订单成功", order.getTransBillNo(), order.getConnBillno());
		data.setCode("0");
		return data;
	}

	private String sendPost(String url, Map<String, String> parameters) {
		String responseContent = null; // 响应内容
		HttpClient httpClient = new DefaultHttpClient(); // 创建默认的httpClient实例
		X509TrustManager xtm = new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			} // 创建TrustManager

		};

		try {
			// TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
			SSLContext ctx = SSLContext.getInstance("TLS");
			// 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[] { xtm }, null);
			// 创建SSLSocketFactory
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
			// 通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
			httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", socketFactory, 443));

			HttpPost httpPost = new HttpPost(url); // 创建HttpPost
			List<NameValuePair> formParams = new ArrayList<NameValuePair>(); // 构建POST请求的表单参数
			for (Map.Entry<String, String> entry : parameters.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));

			HttpResponse response = httpClient.execute(httpPost); // 执行POST请求
			HttpEntity entity = response.getEntity(); // 获取响应实体

			if (null != entity) {
				responseContent = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown(); // 关闭连接,释放资源
			return responseContent;
		}
	}

}
