package com.hitler.payservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.hitler.core.exception.ThirdPayBussinessException;
import com.hitler.core.log.PayLog;
import com.hitler.core.utils.DateUtil;
import com.hitler.core.utils.HttpUtils;
import com.hitler.core.utils.JsonUtil;
import com.hitler.core.utils.MD5Utils;
import com.hitler.core.utils.MapSort;
import com.hitler.core.utils.StringUtils;
import com.hitler.entity.PayBank;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayOrder;
import com.hitler.entity.PayPlatform;
import com.hitler.payservice.support.IpayService;
import com.hitler.payservice.support.PayOrderQueryData;

public class YjfPayService implements IpayService{
	
	public static final String CALLBACK_PAGE_PATH = "/yjf/callback-page";
	public static final String CALLBACK_DATA_PATH = "/yjf/callback-data";
	
	// 查询接口
	public static final String QUERY_ORDER = "http://mp.silverpay.cn/Payapi/index/seachPayOrder";

	@Override
	public Map<String, Object> getPayData(HttpServletRequest request,
			PayOrder order, String payCode) {
		try {
			Map<String, String> map = new TreeMap<String, String>();
			String call = request.getScheme() + "://" + request.getServerName();
			String notifyUrl = call + "/pay/callback" + CALLBACK_DATA_PATH;
			PayLog.getLogger().info("notifyUrl:"+notifyUrl);
			String retUrl = call + "/pay/callback" + CALLBACK_PAGE_PATH;
			PayLog.getLogger().info("retUrl:"+retUrl);
			PayMerchant pm = order.getMerchantId();
			PayPlatform pp = order.getPlatformId();
			map.put("mch_id", pm.getMerchantNo());
			map.put("order_time", System.currentTimeMillis()/1000+""); // 订单时间戳
			map.put("order_number", order.getBillNo().substring(4)); // 供应商订单号
			map.put("goodsname", order.getSummary()); // 订单名称
			map.put("pay_money", Math.round(order.getOrderAmount() * 100) + "");// 订单实际交易金额
			map.put("ret_url", retUrl);
			map.put("notify_url", notifyUrl);
			map.put("paycode", pm.getPaycode());
			map.put("paymenttypeid", payCode);		
			if(payCode.equals("11")){//微信公众号
				map.put("openid", order.getOpenid());
			}
			if(payCode.equals("42")){//网银
				PayBank bank = order.getPayerBankId();
				map.put("defaultbank", bank.getShortName());
			}
			map = MapSort.sortMapByKey(map);
			StringBuilder sb = new StringBuilder();
			for (String key : map.keySet()) {
				sb.append(key+"="+map.get(key)+"&");
			}
			sb.append("key="+pm.getKey());
			System.out.println("sign before:"+sb.toString());
			String sign = MD5Utils.getMD5Str(sb.toString());
			sign = sign.toUpperCase();
			System.out.println("sign after:"+sign);
			map.put("sign", sign);
			for (Map.Entry<String, String> entry : map.entrySet()) {
		       System.out.println(entry.getKey() + " " + entry.getValue());
		    }
			String resp = HttpUtils.sendPost(pp.getPayUrl(), map);
			PayLog.getLogger().info("[云聚付支付返回报文：]"+resp);
			if(payCode.equals("42")){//网银
				Map<String, Object> respMap = new HashMap<String, Object>();
				respMap.put("redirect", 0);
				respMap.put("postUrl", resp);
				return respMap;
			}else{
				YjfResponeData respData = JsonUtil.convertToObject(resp, YjfResponeData.class);
				if(respData.getRet_code().equals("200")){
					Map<String, Object> respMap = new HashMap<String, Object>();
					respMap.put("redirect", 1);
					respMap.put("postUrl", respData.getPayurl());
					respMap.put("transaction_id", respData.getTransaction_id());
					respMap.put("order_number", respData.getOrder_number());
					respMap.put("qrcode", respData.getQrcode());
					respMap.put("qrcodeurl", respData.getQrcodeurl());
					return respMap;
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			PayLog.getLogger().error("[云聚付:{},{}]数据构造失败,产生支付加密数据失败", order.getTransBillNo(), order.getConnBillno(), e);
		}
		return null;
	}

	@Override
	public String getPayOrderNo(HttpServletRequest request) {
		PayLog.getLogger().info("[云聚付]充值页面回调开始");
		getAllParameters(request);
		String orderNumber = request.getParameter("order_number");
		if(StringUtils.isBlank(orderNumber)){
			orderNumber = request.getParameter("order_no");
			if(StringUtils.isBlank(orderNumber)){
				String json = getJsonContent(request);
				PayLog.getLogger().info("[云聚付]充值页面回调json:{}", json);
				YjfResponeData data = JsonUtil.convertToObject(json, YjfResponeData.class);
				orderNumber = data.getOrder_number();
			}
		}
		
		PayLog.getLogger().info("[云聚付:{}]充值回调订单号", orderNumber);
		return orderNumber;
	}
	
	private Map<String, String> getAllParameters(HttpServletRequest request){
		Map<String, String> map = new TreeMap<String, String>();  
        Enumeration<?> paramNames = request.getParameterNames();  
        while (paramNames.hasMoreElements()) {  
            String paramName = (String) paramNames.nextElement();  
  
            String[] paramValues = request.getParameterValues(paramName);  
            if (paramValues.length == 1) {  
                String paramValue = paramValues[0];  
                if (paramValue.length() != 0 && !paramName.equals("sign") && !paramName.equals("ret_msg")) {  
                	PayLog.getLogger().info("参数：" + paramName + "=" + paramValue); 
                    map.put(paramName, paramValue);  
                }  
            }  
        }  
        return map;
	}

	@Override
	public void checkPayBackData(HttpServletRequest request, PayOrder order)
			throws Exception {
		PayMerchant pm = order.getMerchantId();
		Map<String, String> map = getAllParameters(request);
		String mch_id = map.get("mch_id");// 商户号
		String sign = request.getParameter("sign");
		String ret_code = map.get("ret_code");
		if(ret_code.equals("200")){
			map.put("ret_msg", "成功"); 
		}else{
			map.put("ret_msg", "失败"); 
		}
		// 数据返回对象
		if (mch_id == null || "".equals(mch_id)) {
			PayLog.getLogger().error("[云聚付:{},{}]回调失败.商户号为空", order.getTransBillNo(), order.getConnBillno());
			throw new ThirdPayBussinessException("商户号为空");
		}
		if (!pm.getMerchantNo().equals(mch_id)) {
			PayLog.getLogger().error("[云聚付:{},{}]回调失败.商户号不一致,{},{}!", order.getTransBillNo(), order.getConnBillno(),
					pm.getMerchantNo(), mch_id);
			throw new ThirdPayBussinessException("业务参数为空");
		}
		
		map = MapSort.sortMapByKey(map);
		StringBuilder sb = new StringBuilder();
		for (String key : map.keySet()) {
			sb.append(key+"="+map.get(key)+"&");
		}
		sb.append("key="+pm.getKey());
		PayLog.getLogger().info("签名串:"+sb.toString());
		String signState = MD5Utils.getMD5Str(sb.toString());
		signState = signState.toUpperCase();
		PayLog.getLogger().info("签名后:"+signState);
		PayLog.getLogger().info("云聚付签名:"+sign);
		if (!signState.equals(sign)) {
			PayLog.getLogger().error("[云聚付:{},{}]回调失败.服务器数据验签失败!", order.getTransBillNo(), order.getConnBillno());
			throw new ThirdPayBussinessException("服务器数据验签失败");
		}
		if(!ret_code.equals("200")){
			PayLog.getLogger().info("[云聚付:{},{}]支付失败:异步", order.getTransBillNo(),
					order.getConnBillno());
			throw new ThirdPayBussinessException("支付失败");
		}
		
	}
	
	/**
	 * 获取客户端的json请求内容
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getJsonContent(HttpServletRequest request) {

		InputStream i = null;
		StringBuilder sb = null;
		String str = null;
		try {
			i = request.getInputStream();
			int a = 0;
			byte[] bytes = new byte[2048];
			sb = new StringBuilder();
			while ((a = i.read(bytes)) != -1) {
				sb.append(new String(bytes, 0, a, "utf-8"));
			}
			str = URLDecoder.decode(sb.toString(), "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;// sb.toString();
	}

	@Override
	public PayOrderQueryData queryOrder(HttpServletRequest request,
			PayOrder order) throws Exception {
		
		PayOrderQueryData data = new PayOrderQueryData();
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> map = new TreeMap<String, String>();
		PayMerchant pm = order.getMerchantId();
		//PayPlatform pp = order.getPlatformId();
		map.put("mch_id", pm.getMerchantNo());
		parameters.put("mch_id", pm.getMerchantNo());
		map.put("order_time", System.currentTimeMillis()/1000+""); // 订单时间戳
		parameters.put("order_time", System.currentTimeMillis()/1000+""); // 订单时间戳
		map.put("order_number", order.getTransBillNo()); // 供应商订单号
		parameters.put("order_number", order.getTransBillNo()); // 供应商订单号
		map = MapSort.sortMapByKey(map);
		StringBuilder sb = new StringBuilder();
		for (String key : map.keySet()) {
			sb.append(key+"="+map.get(key)+"&");
		}
		sb.append("key="+pm.getKey());
		String sign = MD5Utils.getMD5Str(sb.toString());
		sign = sign.toUpperCase();
		parameters.put("sign", sign);//签名
		String resp = HttpUtils.sendPost(QUERY_ORDER, parameters);
		YjfResponeData reps = JsonUtil.convertToObject(resp, YjfResponeData.class);
		if(reps.getRet_code().equals("200")){
			PayLog.getLogger().error("[云聚付:{},{}]查询订单成功.", order.getTransBillNo(), order.getConnBillno());
			data.setCode("0");
			data.setAmount(order.getFactAmount());
			data.setTradeTime(order.getCreatedDate());
			data.setRespMsg("支付成功");
			if (order.getOrderStatus().equals(PayOrder.OrderStatus.支付成功) == false) {
				// 检查回调信息
				order.setOrderStatus(PayOrder.OrderStatus.支付成功);
				order.setOrderStatusDesc(DateUtil.getNow() + ":" + PayOrder.OrderStatus.支付成功.getName());
				//payOrderService.updateOrderStatus(order);
			}
		}else{
			PayLog.getLogger().error("[云聚付:{},{}]查询订单失败.", order.getTransBillNo(), order.getConnBillno());
			data.setCode("-1");
			data.setRespMsg("支付失败");
			if (order.getOrderStatus().equals(PayOrder.OrderStatus.支付成功) == false
					&& order.getOrderStatus().equals(PayOrder.OrderStatus.支付失败) == false) {
				order.setOrderStatus(PayOrder.OrderStatus.支付失败);
				order.setOrderStatusDesc(reps.getRet_msg());
				//payOrderService.updateOrderStatus(order);
			}
		}
		return data;
	}

}

class YjfResponeData{
	private String ret_code;
	private String ret_msg;
	private String transaction_id;
	private String order_number;
	private String qrcode;
	private String qrcodeurl;
	private String payurl;
	public String getRet_code() {
		return ret_code;
	}
	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}
	public String getRet_msg() {
		return ret_msg;
	}
	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getOrder_number() {
		return order_number;
	}
	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public String getQrcodeurl() {
		return qrcodeurl;
	}
	public void setQrcodeurl(String qrcodeurl) {
		this.qrcodeurl = qrcodeurl;
	}
	public String getPayurl() {
		return payurl;
	}
	public void setPayurl(String payurl) {
		this.payurl = payurl;
	}
}
