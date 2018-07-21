package com.hitler.payservice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.hitler.core.exception.ThirdPayBussinessException;
import com.hitler.core.log.PayLog;
import com.hitler.core.utils.DateUtil;
import com.hitler.core.utils.HttpUtils;
import com.hitler.core.utils.StringUtils;
import com.hitler.entity.PayBank;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayOrder;
import com.hitler.entity.PayPlatform;
import com.hitler.payservice.support.IpayService;
import com.hitler.payservice.support.PayOrderQueryData;
import com.hitler.util.HttpClientUtil;
import com.hitler.util.PayCommonUtil;
/**
 * 酷模支付
 * @author UPC
 *
 */
public class ScanCodePayService implements IpayService{
	
	public static final String CALLBACK_PAGE_PATH = "/kmzf/callback-page";
	
	public static final String CALLBACK_DATA_PATH = "/kmzf/callback-data";
	
	public static final String QUERY_ORDER_URL = "https://open.goodluckchina.net/open/pay/getOrderByOrderId";

	@SuppressWarnings({"unchecked" })
	@Override
	public Map<String, Object> getPayData(HttpServletRequest request,
			PayOrder order, String payCode) {
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		PayMerchant payMerchant = order.getMerchantId();
		//appId	appId	C(32)	必输	
		paramsMap.put("appId", payMerchant.getTerminalNo());
		//商户号
		paramsMap.put("custNo", payMerchant.getMerchantNo());
		//支付方式	payChannel		必输	02=微信；
		paramsMap.put("payChannel", payCode);
		//金额	money	C(10)	必输	单位（元）
		paramsMap.put("money", order.getOrderAmount() + "");
		//paramsMap.put("money", Math.round(order.getOrderAmount()) + "");
		//附加参数回调时传回商户	attach	C(100)	必输	附加参数，可上传商户订单号；
		paramsMap.put("attach", order.getTransBillNo());
		//回调地址	callBackUrl	C(500)	非必填	没有填回调地址则订单状态不通知；
		String call = "http://paytool.mzball.com";
		String notifyUrl = call + "/pay/callback" + CALLBACK_DATA_PATH;
		paramsMap.put("callBackUrl", notifyUrl);
		PayPlatform pp = order.getPlatformId();
		//国付宝银联网关
		if(pp.getPlatformCode().equals("KMYL")){
			//银行编码	bankCode	C(50)	非必填	网关支付时传递该参数直连银行
			PayBank payBank = order.getPayerBankId();
			paramsMap.put("bankCode", payBank.getShortName());
			//银行编码类型	bankCodeType	C(1)	非必填	直连银行类型: 1个人,2企业*/
			paramsMap.put("bankCodeType", "1");
		}
		
		try {
			//签名	sign	C(32)	必输	
			paramsMap.put("sign", generateSignature(paramsMap, payMerchant.getKey()));
			String resp = HttpClientUtil.send(pp.getPayUrl(), paramsMap, "UTF-8");
			//String resp = HttpUtil.Get(url);
			PayLog.getLogger().info("[酷模支付返回报文：]"+resp);
			if(StringUtils.isNotBlank(resp)){
			    Map<String, Object> map = (Map<String, Object>)JSON.parse(resp);  
			    Integer code = (Integer) map.get("code");
			    if(code==1){//成功
			    	Map<String, Object> respMap = new HashMap<String, Object>();
			    	if(payCode.equals("02") || payCode.equals("03")){
			    		respMap.put("qrCode", true);
			    	}
			    	respMap.put("postUrl", map.get("pay_url"));
			    	respMap.put("orderId", map.get("orderId"));
			    	return respMap;
			    }
			}
		} catch (Exception e) {
			PayLog.getLogger().error("[酷模支付:{},{}]下单失败失败", order.getTransBillNo(), order.getConnBillno(), e);
		}
		/*code：接口响应标识
		msg：接口响应说明
		orderNo：我公司订单号
		orderId: 预下单订单Id,特别说明，orderId是商户与平台进行关联的订单唯一标识，商户需要把该Id保存下来，主动查询订单状态的时候用到
		pay_url：不同的payChannel，返回的URL意义可能不一样，具体如下：
		02：返回微信支付二维码URL,将该地址生成二维码即可扫码支付；
		03：返回QQ支付二维码URL，将该地址生成二维码即可扫码支付；
		04：返回网关支付地址，直接请求该地址发起支付；
		05：返回快捷支付地址，直接请求该地址发起支付；*/
		return null;
	}
	
	 private  String generateSignature(final Map<String, String> data, String key) throws Exception{
	        Set<String> keySet = data.keySet();
	        String[] keyArray = keySet.toArray(new String[keySet.size()]);
	        Arrays.sort(keyArray);
	        StringBuilder sb = new StringBuilder();
	        for (String k : keyArray) {
	            if (sb.length()>0) {
	                sb.append("&");
	            }
	            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
	                sb.append(k).append("=").append(data.get(k).trim());
	        }
	        sb.append(key);
	        System.out.println(sb.toString());
	        return PayCommonUtil.MD5(sb.toString()).toLowerCase();
	    }
	 


	@Override
	public String getPayOrderNo(HttpServletRequest request) {
		PayLog.getLogger().info("[酷模支付]充值页面回调开始");
		String orderNumber = request.getParameter("attach");
		PayLog.getLogger().info("[酷模支付:{}]充值回调订单号", orderNumber);
		return orderNumber;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void checkPayBackData(HttpServletRequest request, PayOrder order)
			throws Exception {
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		String sign = null;
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			if("sign".equals(name)){
				PayLog.getLogger().info("mkzf_VVV_sign______"+name+":"+valueStr);
				sign = valueStr;
				continue;
			}
			if(StringUtils.isNotBlank(valueStr)){
				params.put(name, valueStr);
			}
			
			PayLog.getLogger().info("mkzf_params______"+name+":"+valueStr);
		}
		PayMerchant pm = order.getMerchantId();
		try {
			String localSign = generateSignature(params, pm.getKey());
			if(localSign.equals(sign)){
				String pay_status = params.get("pay_status");
				String return_code = params.get("return_code");
				if(return_code.equals("FAIL")){
					PayLog.getLogger().error("[酷模支付:{},{}]回调支付失败", order.getTransBillNo(), order.getConnBillno());
					throw new ThirdPayBussinessException("酷模支付回调支付失败");
				}
				if(pay_status.equals("finish")){//交易结束
					return;
				} else if (pay_status.equals("success")){//支付成功
					return;
				}
			}else{
				PayLog.getLogger().error("[酷模支付:{},{}]回调验证失败", order.getTransBillNo(), order.getConnBillno());
				throw new ThirdPayBussinessException("酷模支付回调验证失败");
			}
		} catch (Exception e) {
			PayLog.getLogger().error("酷模支付订单异步通知Exception:"+e.getMessage(), e);
			throw new ThirdPayBussinessException("酷模支付回调失败");
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		Map<String,String> params = new HashMap<String,String>();
		params.put("pay_status", "success");
		params.put("money", "1.00");
		params.put("cust_no", "gl00023718");
		params.put("trade_no", "20180125141419106898879");
		params.put("pay_channel", "04");
		params.put("return_msg", "支付成功");
		params.put("attach", "180125141419221083");
		params.put("order_id", "4597c8b65c9b4df2b7209c4");
		params.put("return_code", "SUCCESS");
		params.put("pay_time", "20180125142352");
		//params.put("plat_order_no", "");
		String sign = "ee9cbc37a166bc8039ba9ce77";
		ScanCodePayService service = new ScanCodePayService();
		String localSign = service.generateSignature(params, "a8ba215f68ceadf1267aba15949f3b673fb9e5d834ad9ddc7291572bf43d4a7f");
		System.out.println(localSign);
		System.out.println(localSign.equals(sign));
		String str = "attach=180125160022262526&cust_no=gl00023718&money=0.01&order_id=8d4660b37648449ca96804655726cc57&pay_channel=04&pay_status=success&pay_time=20180125160110&return_code=SUCCESS&return_msg=支付成功&trade_no=20180125160023106906438a8ba215f68ceadf1267aba15949f3b673fb9e5d834ad9ddc7291572bf43d4a7f";
		//4b35732137aa4d31953ef0b47bb6b78d
		System.out.println(PayCommonUtil.MD5(str).toLowerCase());
		/*
		[http-nio-4029-exec-9] INFO com.hitler.core.log.PayLog - mkzf_params______plat_order_no:
		[http-nio-4029-exec-9] INFO com.hitler.core.log.PayLog - mkzf_VVV_sign______sign:ee9cbc37a166bc8039ba9ce77
		attach=180125141419221083&cust_no=gl00023718&money=1.00&order_id=4597c8b65c9b4df2b7209c411ce7b84c&pay_chan_msg=支付成功&trade_no=20180125141419106898879a8ba215f68ceadf1267aba15949f3b673fb9e5d834ad9ddc7291572bf43d
*/
	}

	@SuppressWarnings("unchecked")
	@Override
	public PayOrderQueryData queryOrder(HttpServletRequest request,
			PayOrder order) throws Exception {
		Map<String, String> paramsMap = new HashMap<String, String>();
		/*签名	sign		必输	
		appId	appId		必输	
		商户编号	custNo		必输	
		订单Id	orderId		必输	请求支付二维码时返回的orderId；
		咕啦订单号	orderNo		非必须	如果orderId和orderNo同时上传，则会使用orderNo查询*/
		PayMerchant payMerchant = order.getMerchantId();
		//appId	appId	C(32)	必输	
		paramsMap.put("appId", payMerchant.getTerminalNo());
		//商户号
		paramsMap.put("custNo", payMerchant.getMerchantNo());
		//订单Id	orderId
		paramsMap.put("orderId", order.getTransactionId());
		
		PayOrderQueryData data = new PayOrderQueryData();
		try {
			//签名	sign	C(32)	必输	
			paramsMap.put("sign", generateSignature(paramsMap, payMerchant.getKey()));
			String resp = HttpUtils.sendPost(QUERY_ORDER_URL, paramsMap);
			Map<String, Object> map = (Map<String, Object>)JSON.parse(resp);  
			Integer code = (Integer) map.get("code");
			 if(code==1){//成功
				 Map<String, Object> dataMap = (Map<String, Object>) map.get("data");
				/* "orderId": "7c10239d887b4f019b236bf2ab3312ac",//订单唯一Id
				 "money": "100.0",//支付金额
				 "type":00//订单类型 00：D+0；01：D+1；11：T+1
				 "payStatus":00//支付状态（00：未支付；01：支付成功；02：支付失败）*/	
				String  payStatus = (String) dataMap.get("payStatus");
				if(payStatus.equals("01")){
					if (order.getOrderStatus().equals(PayOrder.OrderStatus.支付成功) == false) {
						// 检查回调信息
						order.setOrderStatus(PayOrder.OrderStatus.支付成功);
						order.setOrderStatusDesc(DateUtil.getNow() + ":" + PayOrder.OrderStatus.支付成功.getName());
					}
		        	data.setCode("0");
					data.setAmount(order.getFactAmount());
					data.setTradeTime(order.getCreatedDate());
					data.setRespMsg("支付成功");
				}else if(payStatus.equals("02")){//02：支付失败
		        	if (order.getOrderStatus().equals(PayOrder.OrderStatus.支付成功) == false) {
		        		order.setOrderStatus(PayOrder.OrderStatus.支付失败);
		        		order.setOrderStatusDesc(DateUtil.getNow() + ":" + PayOrder.OrderStatus.支付失败.getName());
		        	}
		        	data.setCode("-1");
					data.setRespMsg("支付失败");
		        }else{
		        	data.setCode("-1");
					data.setRespMsg("等待买家付款");
		        }
			}else{
				data.setCode("-1");
				String msg =  (String) map.get("msg");
				data.setRespMsg("订单查询失败："+msg);
				PayLog.getLogger().info("酷模支付订单查询失败：" + msg);
			 }
		} catch (Exception e) {
			PayLog.getLogger().info("酷模支付订单查询异常：" + e.getMessage(), e);
			data.setCode("-1");
			data.setRespMsg("订单查询异常");
		}
		return data;
	}

}
