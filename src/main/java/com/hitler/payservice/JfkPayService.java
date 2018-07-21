package com.hitler.payservice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hitler.core.exception.ThirdPayBussinessException;
import com.hitler.core.log.PayLog;
import com.hitler.core.utils.HttpUtils;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayOrder;
import com.hitler.entity.PayPlatform;
import com.hitler.payservice.platform.jfk.Base64Local;
import com.hitler.payservice.platform.jfk.JsonUtils;
import com.hitler.payservice.platform.jfk.SecurityRSAPay;
import com.hitler.payservice.support.IpayService;
import com.hitler.payservice.support.PayOrderQueryData;

/**
 * 金付卡支付
 * 
 * @author xu
 *
 */
public class JfkPayService implements IpayService {

	// 付款回调页面地址
	public static final String CALLBACK_PAGE_PATH = "/jfk/callback-page";

	public static final String CALLBACK_DATA_PATH = "/jfk/callback-data";

	// 服务器版本号
	public static String VERSION = "1.0.9";
	// 查询接口
	public static final String QUERY_ORDER = "http://www.goldenpay88.com/gateway/queryPaymentRecord";

	@Override
	public Map<String, Object> getPayData(HttpServletRequest request, PayOrder order, String payCode) {
		Map<String, String> map = new HashMap<String, String>();
		PayMerchant pm = order.getMerchantId();
		PayPlatform pp = order.getPlatformId();
		map.put("terId", pm.getMerchantNo());
		map.put("businessOrdid", order.getBillNo().substring(4)); // 供应商订单号
		map.put("orderName", "jfkPay"); // 订单名称
		map.put("tradeMoney", Math.round(order.getOrderAmount() * 100) + "".trim());// 订单实际交易金额
		map.put("payType", payCode);
		String json = JsonUtils.objectToJson(map);
		PayLog.getLogger().info("[金付卡:{},{}]加密数据:{}", order.getTransBillNo(), order.getConnBillno(),
				JSON.toJSONString(map));
		try {
			byte by[] = SecurityRSAPay.encryptByPublicKey(json.getBytes("utf-8"),
					Base64Local.decode(pm.getPublicKey()));
			String baseStrDec = Base64Local.encodeToString(by, true);
			byte signBy[] = SecurityRSAPay.sign(by, Base64Local.decode((pm.getKey())));
			String sign = Base64Local.encodeToString(signBy, true);

			Map<String, Object> maps = new HashMap<String, Object>();
			maps.put("encParam", baseStrDec);
			maps.put("sign", sign);
			maps.put("merId", pm.getMerchantNo());
			maps.put("version", VERSION);
			maps.put("selfParam", order.getBillNo().substring(4));
			maps.put("postUrl", pp.getPayUrl());
			PayLog.getLogger().info("[金付卡:{},{}]表单数据:{}", order.getTransBillNo(), order.getConnBillno(),
					JSON.toJSONString(maps));
			return maps;
		} catch (Exception e) {
			e.printStackTrace();
			PayLog.getLogger().error("[金付卡:{},{}]数据构造失败,产生支付加密数据失败", order.getTransBillNo(), order.getConnBillno(), e);
		}
		return null;
	}

	@Override
	public String getPayOrderNo(HttpServletRequest request) {
		PayLog.getLogger().info("[金付卡:{}]充值回调订单号", request.getParameter("orderId"));
		return request.getParameter("orderId");
	}

	@Override
	@SuppressWarnings("unchecked")
	public void checkPayBackData(HttpServletRequest request, PayOrder order) throws Exception {
		PayMerchant pm = order.getMerchantId();
		String sign = request.getParameter("sign");// 签名
		String merId = request.getParameter("merId");// 商户号
		String version = request.getParameter("version");// 当前版本
		String encParam = request.getParameter("encParam");// 参数
		// 数据返回对象
		if (merId == null || "".equals(merId)) {
			PayLog.getLogger().error("[金付卡:{},{}]回调失败.商户号为空", order.getTransBillNo(), order.getConnBillno());
			throw new ThirdPayBussinessException("商户号为空");
		}
		if (version == null || "".equals(version)) {
			PayLog.getLogger().error("[金付卡:{},{}]回调失败.版本号为空", order.getTransBillNo(), order.getConnBillno());
			throw new ThirdPayBussinessException("版本号为空");
		}
		if (encParam == null || "".equals(encParam)) {
			PayLog.getLogger().error("[金付卡:{},{}]回调失败.业务参数为空", order.getTransBillNo(), order.getConnBillno());
			throw new ThirdPayBussinessException("业务参数为空");
		}
		if (!merId.equals(merId)) {
			PayLog.getLogger().error("[金付卡:{},{}]回调失败.商户号不一致,{},{}!", order.getTransBillNo(), order.getConnBillno(),
					pm.getMerchantNo(), merId);
			throw new ThirdPayBussinessException("业务参数为空");
		}
		// 验签
		try {
			// 需要根据sign验签
			boolean signState = SecurityRSAPay.verify(Base64Local.decode(encParam),
					Base64Local.decode(pm.getPublicKey()), Base64Local.decode(sign));
			if (!signState) {
				PayLog.getLogger().error("[金付卡:{},{}]回调失败.服务器数据验签失败!", order.getTransBillNo(), order.getConnBillno());
				throw new ThirdPayBussinessException("服务器数据验签失败");
			}
			String decodeJsonStr = new String(
					SecurityRSAPay.decryptByPrivateKey(Base64Local.decode(encParam), Base64Local.decode(pm.getKey())),
					"UTF-8");
			Map<String, String> map = JSON.parseObject(decodeJsonStr, Map.class);
			String orderId = map.get("orderId");
			if (orderId == null || "".equals(orderId)) {
				PayLog.getLogger().error("[金付卡:{},{}]回调失败.服务器数据验签失败!", order.getTransBillNo(), order.getConnBillno());
				throw new ThirdPayBussinessException("解密的充值订单号为空");
			}
			String payOrderId = map.get("payOrderId");
			String order_state = map.get("order_state");
			String money = map.get("money");
			PayLog.getLogger().info("[金付卡:{},{}]校验成功:异步orderId:{},orderState:{},payOrderId:{}", order.getTransBillNo(),
					order.getConnBillno(), orderId, order_state, payOrderId);
			order.setFactAmount(Double.parseDouble(money) / 100);
		} catch (Exception e) {
			PayLog.getLogger().error("[金付卡:{},{}]回调失败.服务器数据验签失败!", order.getTransBillNo(), order.getConnBillno());
			throw new ThirdPayBussinessException("服务器数据验签失败");
		}
	}

	@Override
	public PayOrderQueryData queryOrder(HttpServletRequest request, PayOrder order) throws Exception {
		PayMerchant pm = order.getMerchantId();
		Map<String, String> map = new HashMap<String, String>();
		map.put("businessOrdid", order.getBillNo().substring(4));
		String json = JsonUtils.objectToJson(map);
		PayLog.getLogger().info("[金付卡:{},{}]开始查询订单", order.getTransBillNo(), order.getConnBillno());
		PayOrderQueryData data = new PayOrderQueryData();
		data.setCode("-1");
		try {
			byte by[] = SecurityRSAPay.encryptByPublicKey(json.getBytes("utf-8"),
					Base64Local.decode(pm.getPublicKey()));
			String baseStrDec = Base64Local.encodeToString(by, true);
			byte signBy[] = SecurityRSAPay.sign(by, Base64Local.decode((pm.getKey())));
			String sign = Base64Local.encodeToString(signBy, true);
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("encParam", baseStrDec);
			maps.put("sign", sign);
			maps.put("merId", pm.getMerchantNo());
			maps.put("version", VERSION);
			maps.put("businessOrdid", order.getBillNo().substring(4));
			String resp = HttpUtils.sendPost(QUERY_ORDER, maps);
			PayLog.getLogger().info("[金付卡:{},{}],返回报文:{}", order.getTransBillNo(), order.getConnBillno(), resp);
			JSONObject respJson = JSON.parseObject(resp);
			// 验证签名
			String encParam = respJson.getString("encParam");
			byte[] encBy = Base64Local.decode(encParam);
			byte[] respSign = Base64Local.decode(respJson.getString("sign"));
			boolean tt = SecurityRSAPay.verify(encBy, Base64Local.decode(pm.getPublicKey()), respSign);
			if (!tt) {
				data.setRespMsg("金付卡查询订单失败,原因:验证签名失败");
				return data;
			}
			// 解密参数
			byte[] param = SecurityRSAPay.decryptByPrivateKey(encBy, Base64Local.decode(pm.getKey()));
			String paramStr = new String(param, "utf-8");
			JSONObject paramJson = JSON.parseObject(paramStr);
			String respCode = paramJson.getString("respCode");
			if ("0018".equals(respCode)) {
				data.setRespMsg("金付卡查询订单失败,原因:订单不存在,请到商户后台查询是否有该订单!");
				return data;
			} else if ("1002".equals(respCode)) {
				data.setRespMsg("金付卡查询订单失败,原因:支付失败!");
				return data;
			} else if ("0000".equals(respCode)) {
				data.setRespMsg("金付卡查询订单失败,原因:验签失败!");
				return data;
			} else if ("0001".equals(respCode)) {
				data.setRespMsg("金付卡查询订单失败,原因:商户不存在!");
				return data;
			} else if ("0002".equals(respCode)) {
				data.setRespMsg("金付卡查询订单失败,原因:商户未启用!");
				return data;
			} else if ("0003".equals(respCode)) {
				data.setRespMsg("金付卡查询订单失败,原因:必传参数为空!");
				return data;
			} else if ("0004".equals(respCode)) {
				data.setRespMsg("金付卡查询订单失败,原因:产生订单失败!");
				return data;
			} else if ("0005".equals(respCode)) {
				data.setRespMsg("金付卡查询订单失败,原因:订单已处理过，请勿重复提交!");
				return data;
			} else if ("0006".equals(respCode)) {
				data.setRespMsg("金付卡查询订单失败,原因:单笔超额!");
				return data;
			} else if ("0007".equals(respCode)) {
				data.setRespMsg("金付卡查询订单失败,原因:单日超额!");
				return data;
			} else if ("0010".equals(respCode)) {
				String showMsg = paramJson.getString("show_msg");
				data.setRespMsg("金付卡查询订单失败,原因:失败," + showMsg + "!");
				return data;
			} else if ("0016".equals(respCode)) {
				data.setRespMsg("金付卡查询订单失败,原因:未开通此支付权限!");
				return data;
			} else if ("0017".equals(respCode)) {
				data.setRespMsg("金付卡查询订单失败,原因:系统异常!");
				return data;
			} else if ("0018".equals(respCode)) {
				data.setRespMsg("金付卡查询订单失败,原因:商户订单不存在!");
				return data;
			}
			String orderState = paramJson.getString("order_state");
			if ("1000".equals(respCode)) {
				if ("1001".equals(orderState) || "1002".equals(orderState)) {
					data.setRespMsg("金付卡查询订单失败,原因:未支付!");
					return data;
				} else if ("1004".equals(orderState)) {
					data.setRespMsg("金付卡查询订单失败,原因:已申请退款!");
					return data;
				} else if ("1005".equals(orderState)) {
					data.setRespMsg("金付卡查询订单失败,原因:退款受理成功!");
					return data;
				} else if ("1006".equals(orderState)) {
					data.setRespMsg("金付卡查询订单失败,原因:已退款!");
					return data;
				} else if ("1007".equals(orderState)) {
					data.setRespMsg("金付卡查询订单失败,原因:交易失败!");
					return data;
				} else if ("1008".equals(orderState)) {
					data.setRespMsg("金付卡查询订单失败,原因:退款受理失败!");
					return data;
				} else if ("1009".equals(orderState)) {
					data.setRespMsg("金付卡查询订单失败,原因:已关闭!");
					return data;
				}
				double money = paramJson.getDouble("money");
				Date date = paramJson.getDate("payReturnTime");
				data.setAmount(money / 100);
				data.setTradeTime(date);
				PayLog.getLogger().error("[金付卡:{},{}]查询订单成功.", order.getTransBillNo(), order.getConnBillno());
			}
		} catch (Exception e) {
			PayLog.getLogger().error("[金付卡:{},{}]查询订单失败.{}", order.getTransBillNo(), order.getConnBillno(), e);
			data.setRespMsg(e.getMessage());
			return data;
		}
		data.setCode("0");
		return data;
	}

}
