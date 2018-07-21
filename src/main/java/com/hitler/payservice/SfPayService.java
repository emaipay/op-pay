package com.hitler.payservice;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.hitler.core.exception.ThirdPayBussinessException;
import com.hitler.core.log.PayLog;
import com.hitler.core.utils.DateUtil;
import com.hitler.core.utils.HttpUtils;
import com.hitler.core.utils.StringUtils;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayOrder;
import com.hitler.entity.PayPlatform;
import com.hitler.payservice.support.IpayService;
import com.hitler.payservice.support.PayOrderQueryData;

/**
 * 闪付
 * 
 * @author xu
 *
 */
public class SfPayService implements IpayService {

	// 付款回调页面地址
	public static final String CALLBACK_PAGE_PATH = "/sf/callback-page";

	public static final String CALLBACK_DATA_PATH = "/sf/callback-data";

	private static final String QUERY_ORDER_URL = "http://gw.3yzf.com/v4Query.aspx";
	
//	private static final String test_data_back="http://183.238.56.249:8017/pay/callback/sf/callback-data";
//	private static final String test_page_back="http://183.238.56.249:8017/pay/callback/sf/callback-page";

	@Override
	public Map<String, Object> getPayData(HttpServletRequest request, PayOrder order, String payCode) {
		PayMerchant pm = order.getMerchantId();
		PayPlatform pp = order.getPlatformId();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		try {
			// 当前请求路径
			String reqPath = request.getScheme() + "://" + request.getServerName();
			paramsMap.put("postUrl", pp.getPayUrl());
			String pageUrl = reqPath + "/pay/callback" + CALLBACK_PAGE_PATH;
			String ReturnUrl = reqPath + "/pay/callback" + CALLBACK_DATA_PATH;
			paramsMap.put("MemberID", pm.getMerchantNo());
			// 下单日期
			String date = DateUtil.date2Str(new Date());
			paramsMap.put("TradeDate", date);
			// 商户流水号
			paramsMap.put("TransID", order.getBillNo().substring(4));
			String OrderMoney = order.getOrderAmount() + "";// 订单金额
			if (!"".equals(OrderMoney)) {
				BigDecimal a;
				a = new BigDecimal(OrderMoney).multiply(BigDecimal.valueOf(100)); // 使用分进行提交
				OrderMoney = String.valueOf(a.setScale(0));
			} else {
				OrderMoney = ("0");
			}
			paramsMap.put("OrderMoney", OrderMoney);
			paramsMap.put("PayID", payCode);
			paramsMap.put("AdditionalInfo", "");// 订单附加信息
			paramsMap.put("PageUrl", pageUrl);// 通知商户页面端地址
			paramsMap.put("ReturnUrl", ReturnUrl);// 服务器底层通知地址
			paramsMap.put("NoticeType", "1");// 通知类型
			paramsMap.put("domainName", pm.getDomainName());// 通知类型
			String MARK = "|";
			String md5 = new String(
					pm.getMerchantNo() + MARK + payCode + MARK + date + MARK + order.getBillNo().substring(4) + MARK
							+ OrderMoney + MARK + pageUrl + MARK + ReturnUrl + MARK + "1" + MARK + pm.getKey());// MD5签名格式
			String Signature = signByMD5(md5);// 计算MD5值
			paramsMap.put("Signature", Signature);
			paramsMap.put("TerminalID", pm.getTerminalNo());
			paramsMap.put("InterfaceVersion", "4.0");
			paramsMap.put("KeyType", "1");
			PayLog.getLogger().info("[闪付:{},{}]表单数据:{}", order.getTransBillNo(), order.getConnBillno(),
					JSON.toJSONString(paramsMap));
			return paramsMap;
		} catch (Exception e) {
			PayLog.getLogger().info("[闪付:{},{}]闪付支付出错 ", order.getTransBillNo(), order.getConnBillno(), e);
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
	private String signByMD5(String sourceData) throws Exception {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(sourceData.getBytes("utf-8"));
			byte[] digest = md5.digest();

			StringBuffer hexString = new StringBuffer();
			String strTemp;
			for (int i = 0; i < digest.length; i++) {
				// byteVar &
				// 0x000000FF的作用是，如果digest[i]是负数，则会清除前面24个零，正的byte整型不受影响。
				// (...) | 0xFFFFFF00的作用是，如果digest[i]是正数，则置前24位为一，
				// 这样toHexString输出一个小于等于15的byte整型的十六进制时，倒数第二位为零且不会被丢弃，这样可以通过substring方法进行截取最后两位即可。
				strTemp = Integer.toHexString((digest[i] & 0x000000FF) | 0xFFFFFF00).substring(6);
				hexString.append(strTemp);
			}
			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public String getPayOrderNo(HttpServletRequest request) {
		PayLog.getLogger().info("[闪付:{}]回调订单号", request.getParameter("TransID"));
		return request.getParameter("TransID");
	}

	@Override
	public void checkPayBackData(HttpServletRequest request, PayOrder order) throws Exception {
		PayMerchant pm = order.getMerchantId();
		try {
			String MemberID = request.getParameter("MemberID");// 商户号
			String TerminalID = request.getParameter("TerminalID");// 商户终端号
			String TransID = request.getParameter("TransID");// 商户流水号
			String Result = request.getParameter("Result");// 支付结果
			String ResultDesc = request.getParameter("ResultDesc");// 支付结果描述
			if (StringUtils.isEmpty(ResultDesc)) {
				ResultDesc = request.getParameter("resultDesc");
			}
			String FactMoney = request.getParameter("FactMoney");// 实际成功金额
			if (StringUtils.isEmpty(FactMoney)) {
				FactMoney = request.getParameter("factMoney");
			}
			String AdditionalInfo = request.getParameter("AdditionalInfo");// 订单附加消息
			if (StringUtils.isEmpty(FactMoney)) {
				AdditionalInfo = request.getParameter("additionalInfo");
			}
			String SuccTime = request.getParameter("SuccTime");// 支付完成时间
			String Md5Sign = request.getParameter("Md5Sign");// MD5签名
			String MARK = "~|~";
			String md5 = "MemberID=" + MemberID + MARK + "TerminalID=" + TerminalID + MARK + "TransID=" + TransID + MARK
					+ "Result=" + Result + MARK + "ResultDesc=" + ResultDesc + MARK + "FactMoney=" + FactMoney + MARK
					+ "AdditionalInfo=" + AdditionalInfo + MARK + "SuccTime=" + SuccTime + MARK + "Md5Sign="
					+ pm.getKey();
			PayLog.getLogger().info("[闪付:{},{}]检查闪付数据:{}", order.getTransBillNo(), order.getConnBillno(), md5);
			String WaitSign = signByMD5(md5);
			if (WaitSign.equals(Md5Sign)) {
				if (StringUtils.equalsIgnoreCase(pm.getMerchantNo(), MemberID) == false) {
					throw new ThirdPayBussinessException("商户号不一致" + pm.getMerchantNo() + ":" + MemberID);
				}
				double realFactMoney = Double.parseDouble(FactMoney);
				String orderAmount = order.getOrderAmount() + "";
				if (!"".equals(orderAmount)) {
					BigDecimal a;
					a = new BigDecimal(orderAmount).multiply(BigDecimal.valueOf(100)); // 使用分进行提交
					orderAmount = String.valueOf(a.setScale(0));
				} else {
					orderAmount = ("0");
				}
				if (Double.parseDouble(orderAmount) != realFactMoney) {
					throw new ThirdPayBussinessException(
							"实际成交金额与您提交的订单金额不一致:" + order.getOrderAmount() + ":" + realFactMoney);
				}
				PayLog.getLogger().info("[闪付:{},{}]校验通过.", order.getTransBillNo(), order.getConnBillno());
				order.setFactAmount(order.getOrderAmount());

			} else {
				throw new ThirdPayBussinessException("非法数据:" + md5);
			}
		} catch (Exception e) {
			e.printStackTrace();
			PayLog.getLogger().info("[闪付:{},{}]异常.", order.getTransBillNo(), order.getConnBillno(), e);
			throw new ThirdPayBussinessException("加密异常:" + e.getMessage());
		}

	}

	@Override
	public PayOrderQueryData queryOrder(HttpServletRequest request, PayOrder order) throws Exception {
		PayLog.getLogger().info("[闪付:{},{}]开始查询订单.", order.getTransBillNo(), order.getConnBillno());
		PayMerchant pm = order.getMerchantId();
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("MemberID", pm.getMerchantNo());
		// 商户流水号
		paramsMap.put("TransID", order.getBillNo().substring(4));
		paramsMap.put("TerminalID", pm.getTerminalNo());
		String MARK = "|";
		String md5 = new String(pm.getMerchantNo() + MARK + pm.getTerminalNo() + MARK + order.getBillNo().substring(4)
				+ MARK + pm.getKey());// MD5签名格式
		PayOrderQueryData data = new PayOrderQueryData();
		data.setCode("-1");
		try {
			String sign = signByMD5(md5);
			paramsMap.put("Md5Sign", sign);
			String resp = HttpUtils.sendPost(QUERY_ORDER_URL, paramsMap);
			PayLog.getLogger().info("[闪付:{},{}]返回数据:{}", order.getTransBillNo(), order.getConnBillno(), resp);
			if (resp.indexOf("|") < 0) {
				data.setRespMsg("请求闪付失败");
				return data;
			}

			String[] result = resp.split("\\|");
			if (!"Y".equals(result[3].split("=")[1])) {
				if ("F".equalsIgnoreCase(result[3].split("=")[1])) {
					data.setRespMsg("订单查询失败:支付失败");
					return data;
				} else if ("P".equalsIgnoreCase(result[3].split("=")[1])) {
					data.setRespMsg("订单查询失败:处理中.请到闪付商户后台查询支付是否成功！");
					return data;
				} else if ("N".equalsIgnoreCase(result[3].split("=")[1])) {
					data.setRespMsg("订单查询失败:没有查询到该订单.");
					return data;
				}
				data.setRespMsg("未知错误");
				return data;
			}
			String orderAmount = result[4].split("=")[1];
			String merNos = result[0].split("=")[1];
			if (!merNos.equals(pm.getMerchantNo())) {
				data.setRespMsg("订单查询失败,查询的商户号不一致.");
				return data;
			}
			if (Double.parseDouble(orderAmount) / 100 != order.getOrderAmount()) {
				data.setRespMsg("订单查询失败,查询订单的金额平台保存的不一致.");
				return data;
			}
			data.setAmount(Double.parseDouble(orderAmount) / 100);
			if (StringUtils.isNotEmpty(result[5].split("=")[1])) {
				data.setTradeTime(DateUtil.str2Date(result[5].split("=")[1]));
			}
			PayLog.getLogger().info("[闪付:{},{}]查询订单成功.", order.getTransBillNo(), order.getConnBillno());
		} catch (Exception e) {
			e.printStackTrace();
			data.setRespMsg(e.getMessage());
			return data;
		} // 计算MD5值
		data.setCode("0");
		return data;
	}

}
