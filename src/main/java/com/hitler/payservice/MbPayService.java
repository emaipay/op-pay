package com.hitler.payservice;

import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.hitler.core.exception.ThirdPayBussinessException;
import com.hitler.core.log.PayLog;
import com.hitler.core.utils.DateUtil;
import com.hitler.core.utils.StringUtils;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayOrder;
import com.hitler.entity.PayPlatform;
import com.hitler.payservice.platform.mb.MbUtils;
import com.hitler.payservice.platform.mb.QueryResponseEntity;
import com.hitler.payservice.support.IpayService;
import com.hitler.payservice.support.PayOrderQueryData;

/**
 * 摩宝
 * 
 * @author xu
 *
 */
public class MbPayService implements IpayService {
	public static final String MOBAOPAY_APINAME_PAY = "WEB_PAY_B2C";
	public static final String MOBO_TRAN_QUERY = "MOBO_TRAN_QUERY";// 查询订单接口
	public static final String MOBAOPAY_API_VERSION = "1.0.0.0";
	// 付款回调页面地址
	public static final String CALLBACK_PAGE_PATH = "/mb/callback-page";

	@Override
	public Map<String, Object> getPayData(HttpServletRequest request, PayOrder order, String payCode) {
		PayMerchant pm = order.getMerchantId();
		PayPlatform pp = order.getPlatformId();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		// 当前请求路径
		String reqPath = request.getScheme() + "://" + request.getServerName();
		String callbackUrl = reqPath + "/pay/callback" + CALLBACK_PAGE_PATH;
		paramsMap.put("apiName", MOBAOPAY_APINAME_PAY);
		paramsMap.put("apiVersion", MOBAOPAY_API_VERSION);
		paramsMap.put("platformID", pm.getTerminalNo());
		paramsMap.put("merchNo", pm.getMerchantNo());
		paramsMap.put("orderNo", order.getBillNo());
		paramsMap.put("tradeDate", DateUtil.dateToStrOnRi(order.getCreatedDate()));
		paramsMap.put("amt", order.getOrderAmount() + "");
		paramsMap.put("merchUrl", callbackUrl);
		paramsMap.put("merchParam", order.getBillNo().substring(4));
		paramsMap.put("tradeSummary", "Mopay");
		paramsMap.put("postUrl", pp.getPayUrl());
		paramsMap.put("domainName", pm.getDomainName());
		/**
		 * bankCode为空，提交表单后浏览器在新窗口显示支付系统收银台页面，在这里可以通过账户余额支付或者选择银行支付；
		 * bankCode不为空，取值只能是接口文档中列举的银行代码，提交表单后浏览器将在新窗口直接打开选中银行的支付页面。
		 * 无论选择上面两种方式中的哪一种，支付成功后收到的通知都是同一接口。
		 **/
		if ("1".equals(payCode) || "5".equals(payCode)) {
			paramsMap.put("choosePayType", payCode);
			paramsMap.put("bankCode", null);
		} else {
			paramsMap.put("bankCode", payCode);
			paramsMap.put("choosePayType", "");
		}
		try {
			String sourceData = generatePayRequest(paramsMap);
			String signMsg = signData(sourceData, pm.getKey());
			paramsMap.put("signMsg", signMsg);
			PayLog.getLogger().info("[摩宝:{},{}]表单数据:{}", order.getTransBillNo(), order.getConnBillno(),
					JSON.toJSONString(paramsMap));
			return paramsMap;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 生成签名
	 * 
	 * @param sourceData
	 * @return
	 * @throws Exception
	 */
	private String signData(String sourceData, String md5Key) throws Exception {
		if (StringUtils.isBlank(sourceData)) {
			throw new Exception("签名数据源串为空！");
		}
		String signStrintg = signByMD5(sourceData, md5Key);
		return signStrintg;
	}

	private String signByMD5(String sourceData, String key) throws Exception {
		String data = sourceData + key;
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] sign = md5.digest(data.getBytes("UTF-8"));
		return Bytes2HexString(sign).toUpperCase();
	}

	/**
	 * 将byte数组转成十六进制的字符串
	 * 
	 * @param b
	 *            byte[]
	 * @return String
	 */
	private String Bytes2HexString(byte[] b) {
		StringBuffer ret = new StringBuffer(b.length);
		String hex = "";
		for (int i = 0; i < b.length; i++) {
			hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret.append(hex.toUpperCase());
		}
		return ret.toString();
	}

	/**
	 * 将由支付请求参数构成的map转换成支付串，并对参数做合法验证
	 * 
	 * @param paramsMap
	 *            由支付请求参数构成的map
	 * @return
	 * @throws Exception
	 */
	private String generatePayRequest(Map<String, Object> paramsMap) throws Exception {
		// 验证输入数据合法性
		if (!paramsMap.containsKey("apiName")) {
			throw new ThirdPayBussinessException("apiName不能为空");
		}
		if (!paramsMap.containsKey("apiVersion")) {
			throw new ThirdPayBussinessException("apiVersion不能为空");
		}
		if (!paramsMap.containsKey("platformID")) {
			throw new ThirdPayBussinessException("platformID不能为空");
		}
		if (!paramsMap.containsKey("merchNo")) {
			throw new ThirdPayBussinessException("merchNo不能为空");
		}
		if (!paramsMap.containsKey("orderNo")) {
			throw new ThirdPayBussinessException("orderNo不能为空");
		}
		if (!paramsMap.containsKey("tradeDate")) {
			throw new ThirdPayBussinessException("tradeDate不能为空");
		}
		if (!paramsMap.containsKey("amt")) {
			throw new ThirdPayBussinessException("amt不能为空");
		}
		if (!paramsMap.containsKey("merchUrl")) {
			throw new ThirdPayBussinessException("merchUrl不能为空");
		}
		if (!paramsMap.containsKey("merchParam")) {
			throw new ThirdPayBussinessException("merchParam可以为空，但必须存在！");
		}
		if (!paramsMap.containsKey("tradeSummary")) {
			throw new ThirdPayBussinessException("tradeSummary不能为空");
		}
		// 输入数据组织成字符串
		String paramsStr = String.format(
				"apiName=%s&apiVersion=%s&platformID=%s&merchNo=%s&orderNo=%s&tradeDate=%s&amt=%s&merchUrl=%s&merchParam=%s&tradeSummary=%s",
				paramsMap.get("apiName"), paramsMap.get("apiVersion"), paramsMap.get("platformID"),
				paramsMap.get("merchNo"), paramsMap.get("orderNo"), paramsMap.get("tradeDate"), paramsMap.get("amt"),
				paramsMap.get("merchUrl"), paramsMap.get("merchParam"), paramsMap.get("tradeSummary"));
		return paramsStr;
	}

	@Override
	public String getPayOrderNo(HttpServletRequest request) {
		PayLog.getLogger().info("[摩宝:{}]返回订单号", request.getParameter("merchParam"));
		return request.getParameter("merchParam");
	}

	@Override
	public void checkPayBackData(HttpServletRequest request, PayOrder order) throws Exception {
		PayLog.getLogger().info("[摩宝:{},{}]开始校验数据", order.getTransBillNo(), order.getConnBillno());
		String merchParam = request.getParameter("merchParam");
		try {
			request.setCharacterEncoding("utf-8");
			String apiName = request.getParameter("apiName");
			String notifyTime = request.getParameter("notifyTime");
			String tradeAmt = request.getParameter("tradeAmt");
			String merchNo = request.getParameter("merchNo");
			String orderNo = request.getParameter("orderNo");
			String tradeDate = request.getParameter("tradeDate");
			String accNo = request.getParameter("accNo");
			String accDate = request.getParameter("accDate");
			String orderStatus = request.getParameter("orderStatus");
			String signMsg = request.getParameter("signMsg");
			signMsg.replaceAll(" ", "\\+");
			String srcMsg = String.format(
					"apiName=%s&notifyTime=%s&tradeAmt=%s&merchNo=%s&merchParam=%s&orderNo=%s&tradeDate=%s&accNo=%s&accDate=%s&orderStatus=%s",
					apiName, notifyTime, tradeAmt, merchNo, merchParam, orderNo, tradeDate, accNo, accDate,
					orderStatus);
			PayLog.getLogger().info("[摩宝:{},{}]检查摩宝数据:{}", order.getTransBillNo(), order.getConnBillno(), srcMsg);
			// 商家
			PayMerchant pm = order.getMerchantId();
			// 验证签名
			boolean verifyRst = verifyData(signMsg, srcMsg, pm.getKey());
			if (verifyRst) {
				String merchantCode = request.getParameter("merchNo");
				if (StringUtils.equalsIgnoreCase(pm.getMerchantNo(), merchantCode) == false) {
					throw new ThirdPayBussinessException("商户号不一致" + pm.getMerchantNo() + ":" + merchantCode);
				}
				double realFactMoney = Double.parseDouble(tradeAmt);
				if (order.getOrderAmount() != realFactMoney) {
					throw new ThirdPayBussinessException(
							"实际成交金额与您提交的订单金额不一致:" + order.getOrderAmount() + ":" + realFactMoney);
				}
				order.setFactAmount(order.getOrderAmount());
				PayLog.getLogger().info("[摩宝:{},{}]校验通过:{}", order.getTransBillNo(), order.getConnBillno(), srcMsg);
			} else {
				throw new ThirdPayBussinessException("非法签名");
			}
		} catch (Exception e) {
			PayLog.getLogger().error("[摩宝:{},{}]校验异常,参数:{}", order.getTransBillNo(), order.getConnBillno(),
					merchParam);
			throw new ThirdPayBussinessException("摩宝充值,校验异常:" + e.getMessage());
		}
	}

	/**
	 * 验证签名
	 * 
	 * @param signData
	 *            签名数据
	 * @param srcData
	 *            原数据
	 * @return
	 * @throws Exception
	 */
	private boolean verifyData(String signData, String srcData, String md5Key) throws Exception {

		if (signData.equalsIgnoreCase(signByMD5(srcData, md5Key))) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public PayOrderQueryData queryOrder(HttpServletRequest request,PayOrder order) throws Exception {
		PayLog.getLogger().info("[摩宝:{},{}]开始查询订单", order.getTransBillNo(), order.getConnBillno());
		PayMerchant pm = order.getMerchantId();
		PayPlatform pp = order.getPlatformId();
		PayOrderQueryData data = new PayOrderQueryData();
		data.setCode("-1");
		try {
			// 准备输入数据
			Map<String, String> requestMap = new HashMap<String, String>();
			requestMap.put("apiName", MOBO_TRAN_QUERY);
			requestMap.put("apiVersion", MOBAOPAY_API_VERSION);
			requestMap.put("platformID", pm.getTerminalNo());
			requestMap.put("merchNo", pm.getMerchantNo());
			requestMap.put("orderNo", order.getBillNo());
			requestMap.put("tradeDate", DateUtil.dateToStrOnRi(order.getCreatedDate()));
			requestMap.put("amt", order.getOrderAmount() + "");
			String paramsStr = generateQueryRequest(requestMap);
			String signStr = signData(paramsStr, pm.getKey());
			paramsStr = paramsStr + "&signMsg=" + signStr;
			PayLog.getLogger().info("[摩宝:{},{}]请求参数:{}", order.getTransBillNo(), order.getConnBillno(),
					paramsStr);
			// 发送请求并接收返回
			String responseMsg = MbUtils.transact(paramsStr, pp.getPayUrl());
			// 解析返回数据
			QueryResponseEntity entity = new QueryResponseEntity();
			String strData = entity.parse(responseMsg);
			PayLog.getLogger().info("[摩宝:{},{}]订单返回数据:{}", order.getTransBillNo(), order.getConnBillno(),
					strData);
			if (!verifyData(entity.getSignMsg(), strData, pm.getKey())) {
				data.setRespMsg("签名验证不通过");
				return data;
			}
			data.setAmount(order.getOrderAmount());
			data.setTradeTime(new Date());
			if (StringUtils.isBlank(responseMsg)) {
				data.setRespMsg("摩宝返回结果为空");
				return data;
			} else {
				// 0-未支付1-成功2-失败
				// 4-部分退款5-全额退款
				// 9-退款处理中10-未支付 11-订单过期
				if (!"00".equals(entity.getRespCode())) {
					data.setRespMsg("查询订单失败:" + entity.getRespDesc());
					return data;
				}
				if (!"1".equals(entity.getStatus())) {
					if ("0".equals(entity.getStatus())) {
						data.setRespMsg("订单未支付");
						return data;
					} else if ("2".equals(entity.getStatus())) {
						data.setRespMsg("订单支付失败");
						return data;
					} else if ("4".equals(entity.getStatus())) {
						data.setRespMsg("订单部分退款");
						return data;
					} else if ("5".equals(entity.getStatus())) {
						data.setRespMsg("订单全额退款");
						return data;
					} else if ("9".equals(entity.getStatus())) {
						data.setRespMsg("订单退款处理中");
						return data;
					} else if ("10".equals(entity.getStatus())) {
						data.setRespMsg("订单未支付");
						return data;
					} else if ("11".equals(entity.getStatus())) {
						data.setRespMsg("订单过期");
						return data;
					}
				}
			}
		} catch (Exception e) {
			PayLog.getLogger().error("[摩宝:{},{}]查询订单失败", order.getTransBillNo(), order.getConnBillno(), e);
			data.setRespMsg("查询失败:" + e.getMessage());
			return data;
		}
		data.setCode("0");
		return data;
	}

	/**
	 * 将由查询请求参数组成的map组织成字符串，并对参数做合法性验证
	 * 
	 * @param paramsMap
	 * @return
	 * @throws Exception
	 */
	public static String generateQueryRequest(Map<String, String> paramsMap) throws Exception {
		if (!paramsMap.containsKey("apiName") || StringUtils.isBlank(paramsMap.get("apiName"))) {
			throw new Exception("apiName不能为空");
		}
		if (!paramsMap.containsKey("apiVersion") || StringUtils.isBlank(paramsMap.get("apiVersion"))) {
			throw new Exception("apiVersion不能为空");
		}
		if (!paramsMap.containsKey("platformID") || StringUtils.isBlank(paramsMap.get("platformID"))) {
			throw new Exception("platformID不能为空");
		}
		if (!paramsMap.containsKey("merchNo") || StringUtils.isBlank(paramsMap.get("merchNo"))) {
			throw new Exception("merchNo不能为空");
		}
		if (!paramsMap.containsKey("orderNo") || StringUtils.isBlank(paramsMap.get("orderNo"))) {
			throw new Exception("orderNo不能为空");
		}
		if (!paramsMap.containsKey("tradeDate") || StringUtils.isBlank(paramsMap.get("tradeDate"))) {
			throw new Exception("tradeDate不能为空");
		}
		if (!paramsMap.containsKey("amt") || StringUtils.isBlank(paramsMap.get("amt"))) {
			throw new Exception("amt不能为空");
		}

		String resultStr = String.format(
				"apiName=%s&apiVersion=%s&platformID=%s&merchNo=%s&orderNo=%s&tradeDate=%s&amt=%s",
				paramsMap.get("apiName"), paramsMap.get("apiVersion"), paramsMap.get("platformID"),
				paramsMap.get("merchNo"), paramsMap.get("orderNo"), paramsMap.get("tradeDate"), paramsMap.get("amt"));
		return resultStr;
	}

}
