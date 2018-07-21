package com.hitler.payservice;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.hitler.core.exception.ThirdPayBussinessException;
import com.hitler.core.log.PayLog;
import com.hitler.core.utils.DateUtil;
import com.hitler.core.utils.HttpUtils;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayOrder;
import com.hitler.payservice.support.IpayService;
import com.hitler.payservice.support.PayOrderQueryData;
import com.hitler.util.SHA256Util;
/**
 * 微信支付
 * @author FredCheng
 *
 */
public class SmilePayService implements IpayService{
	
	// 付款回调页面地址
	public static final String CALLBACK_PAGE_PATH = "/smile/callback-page";

	public static final String CALLBACK_DATA_PATH = "/smile/callback-data";

	public static final String QUERY_ORDER_URL = "http://pay.smilepay.vip/v10/query/";

	@Override
	public Map<String, Object> getPayData(HttpServletRequest request,
			PayOrder order, String payCode) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		PayMerchant payMerchant = order.getMerchantId();
		//商户号	merchantId	是	string(20)	商户识别号，由微笑支付分配
		paramsMap.put("merchantId", payMerchant.getMerchantNo());
		//支付方式	payMode	是	string(10)	
		paramsMap.put("payMode", payCode);
		//商户订单号	orderNo	是	string(20)	商户网站唯一订单号
		paramsMap.put("orderNo", order.getTransBillNo());
		//总金额	orderAmount	是	double	商户订单总金额，最小单位：分。例：12.34
		paramsMap.put("orderAmount",  order.getOrderAmount() + "");
		//商品名称	goods	是	string(50)	商品名称，可留空
		paramsMap.put("goods", "");
		//后台通知地址	notifyUrl	是	string(255)	接收微笑支付通知的url，需给绝对路径，255字符内格式
		//如:http://wap.abc.com/notify.php，确保通过互联网可访问该地址
		String call = "http://paytool.mzball.com";
		String notifyUrl = call + "/pay/callback" + CALLBACK_DATA_PATH;
		paramsMap.put("notifyUrl", notifyUrl);
		/*交易后返回商户地址	returnUrl	是	string(255)	交易完成后返回商户网站的地址，需给绝对路径，255字符内格式
		如:http://wap.abc.com/return.php，确保通过互联网可访问该地址
		 */		
		String returnUrl = call + "/pay/callback" + CALLBACK_PAGE_PATH;
		paramsMap.put("returnUrl", returnUrl);
		/*银行代码	bank	是	string(10)	银行代码，请参照银行代码表
		银行卡支付时如空白，页面转至微笑支付收银台，
		填写时直接跳转至银行页面。其他支付方式留空*/
		paramsMap.put("bank", "");
		//签名方式	encodeType	是	string(10)	固定值：SHA2
		paramsMap.put("encodeType", "SHA2");
		try {
			//签名	signSHA2	否	string(128)	签名结果，详见“第4 章 签名规则”
			String signSHA2 = generateSignature(paramsMap, payMerchant.getKey(), payMerchant.getPublicKey());
			paramsMap.put("signSHA2", signSHA2);
			// 调用统一下单接口
			String resp = HttpUtils.sendPost(payMerchant.getPlatformId().getPayUrl(), paramsMap);
			PayLog.getLogger().info("微笑支付"+payCode+"响应报文：" + resp);
			Map<String, Object> respMap = new HashMap<String, Object>();
			respMap.put("redirect", 0);
			respMap.put("postUrl", resp);
			return respMap;
		} catch (Exception e) {
			PayLog.getLogger().error("[微笑支付:{},{}]下单失败失败", order.getTransBillNo(), order.getConnBillno(), e);
		}
		return null;
	}
	
	 private static  String sortAndEncodeParams(final Map<String, String> data, String sha2Key, String hashIV) throws UnsupportedEncodingException{
	       Set<String> keySet = data.keySet();
	       String[] keyArray = keySet.toArray(new String[keySet.size()]);
	       Arrays.sort(keyArray);
	       StringBuilder sb = new StringBuilder();
	       for (String k : keyArray) {
	            if (sb.length()>0) {
	                sb.append("&");
	            }
	            sb.append(k).append("=").append(data.get(k).trim());// 参数值为空，则不参与签名
	       }
	     //  PayLog.getLogger().info(sb.toString());
	       String sortParams = "SHA2Key="+sha2Key+"&"+sb.toString()+"&HashIV="+hashIV;
	       PayLog.getLogger().info("sortParams："+sortParams);
	       //3.进行URLEncode编码，并转成小写字符
	       sortParams = URLEncoder.encode(sortParams, "UTF-8").toLowerCase();
	       PayLog.getLogger().info("urlString："+sortParams);
	       return sortParams;
	 }
	 
	 public  static String generateSignature(final Map<String, String> data, String sha2Key, String hashIV) throws Exception{
		// String urlString = sortAndEncodeParams(data, sha2Key, hashIV);
		 String signSHA2 = SHA256Util.getSHA256StrJava(sortAndEncodeParams(data, sha2Key, hashIV)).toUpperCase();
		 PayLog.getLogger().info("signSHA2："+signSHA2);
		 return signSHA2;
	 }

	@Override
	public String getPayOrderNo(HttpServletRequest request) {
		PayLog.getLogger().info("[微笑支付]充值页面回调开始");
		String orderNo = request.getParameter("orderNo");
		PayLog.getLogger().info("[微笑支付:{}]充值回调订单号", orderNo);
		return orderNo;
	}

	@Override
	public void checkPayBackData(HttpServletRequest request, PayOrder order)
			throws Exception {
		//获取微笑支付POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		PayMerchant payMerchant = order.getMerchantId();
		//商户号	merchantId	是	string(20)	商户识别号，由微笑支付分配
		PayLog.getLogger().info("smile_params______merchantId:"+request.getParameter("merchantId"));
		params.put("merchantId", payMerchant.getMerchantNo());
		//支付方式	payMode	是	string(10)	银行卡：Bank
		String payMode = request.getParameter("payMode");
		PayLog.getLogger().info("smile_params______payMode:"+payMode);
		params.put("payMode", payMode);
		//商户订单号	orderNo	是	string(20)	商户网站唯一订单号
		String orderNo = request.getParameter("orderNo");
		PayLog.getLogger().info("smile_params______orderNo:"+orderNo);
		params.put("orderNo", orderNo);
		//总金额	orderAmount	是	double	商户订单总金额，最小单位：分。例：12.34
		String orderAmount = request.getParameter("orderAmount");
		PayLog.getLogger().info("smile_params______orderAmount:"+orderAmount);
		params.put("orderAmount", orderAmount);
		//微笑支付订单号	tradeNo	是	string(32)	微笑支付平台唯一订单识别码
		String tradeNo = request.getParameter("tradeNo");
		PayLog.getLogger().info("smile_params______tradeNo:"+tradeNo);
		params.put("tradeNo", tradeNo);
		//交易结果	success	否	string(1)	Y：交易成功	F：交易失败
		String success = request.getParameter("success");
		PayLog.getLogger().info("smile_params______success:"+success);
		//签名方式	encodeType	是	string(10)	固定值：SHA2
		String encodeType = request.getParameter("encodeType");
		PayLog.getLogger().info("smile_params______encodeType:"+encodeType);
		params.put("encodeType", encodeType);
		//签名	signSHA2	否	string(128)	签名结果，详见“第4 章 签名规则
		String signSHA2 = request.getParameter("signSHA2");
		PayLog.getLogger().info("smile_params______signSHA2:"+signSHA2);
		try {
			String localSign = generateSignature(params, payMerchant.getKey(), payMerchant.getPublicKey());
			if(localSign.equals(signSHA2)){
				if(success.equals("F")){
					PayLog.getLogger().error("[微笑支付:{},{}]回调支付失败", order.getTransBillNo(), order.getConnBillno());
					throw new ThirdPayBussinessException("微笑支付回调支付失败");
				}
				return;
			}else{
				PayLog.getLogger().error("[微笑支付:{},{}]回调验证失败", order.getTransBillNo(), order.getConnBillno());
				throw new ThirdPayBussinessException("微笑支付回调验证失败");
			}
		} catch (Exception e) {
			PayLog.getLogger().error("微笑支付订单异步通知Exception:"+e.getMessage(), e);
			throw new ThirdPayBussinessException("微笑支付回调失败");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public PayOrderQueryData queryOrder(HttpServletRequest request,
			PayOrder order) throws Exception {
		Map<String, String> paramsMap = new HashMap<String, String>();
		PayMerchant payMerchant = order.getMerchantId();
		//商户号	merchantId	是	string(20)	商户识别号，由微笑支付分配
		paramsMap.put("merchantId", payMerchant.getMerchantNo());
		//支付方式	payMode	是	string(10)	
		paramsMap.put("payMode", payMerchant.getPaycode());
		//商户订单号	orderNo	是	string(20)	商户网站唯一订单号
		paramsMap.put("orderNo", order.getTransBillNo());
		//签名方式	encodeType	是	string(10)	固定值：SHA2
		paramsMap.put("encodeType", "SHA2");
		
		PayOrderQueryData data = new PayOrderQueryData();
		try {
			//签名	signSHA2	否	string(128)	签名结果，详见“第4 章 签名规则
			String signSHA2 = generateSignature(paramsMap, payMerchant.getKey(), payMerchant.getPublicKey());
			paramsMap.put("signSHA2", signSHA2);
			String resp = HttpUtils.sendPost(QUERY_ORDER_URL, paramsMap);
			Map<String, Object> map = (Map<String, Object>)JSON.parse(resp);  
			
			String code = (String) map.get("code");
			 if(code.equals("00")){//00表示成功，非00表示失败 此字段是通信标识，非交易标识，交易是否成功需要查看success来判断
				 Map<String, Object> dataMap = (Map<String, Object>) map.get("data");
				// 交易结果	success	是	string(1)	Y：交易成功F：交易失败	
				String  success = (String) dataMap.get("success");
				if(success.equals("Y")){
					if (order.getOrderStatus().equals(PayOrder.OrderStatus.支付成功) == false) {
						// 检查回调信息
						order.setOrderStatus(PayOrder.OrderStatus.支付成功);
						order.setOrderStatusDesc(DateUtil.getNow() + ":" + PayOrder.OrderStatus.支付成功.getName());
					}
		        	data.setCode("0");
					data.setAmount(order.getFactAmount());
					data.setTradeTime(order.getCreatedDate());
					data.setRespMsg("支付成功");
				}else{//F：交易失败
		        	if (order.getOrderStatus().equals(PayOrder.OrderStatus.支付成功) == false) {
		        		order.setOrderStatus(PayOrder.OrderStatus.支付失败);
		        		order.setOrderStatusDesc(DateUtil.getNow() + ":" + PayOrder.OrderStatus.支付失败.getName());
		        	}
		        	data.setCode("-1");
					data.setRespMsg("支付失败");
		        }
			}else{
				data.setCode("-1");
				String msg =  (String) map.get("msg");
				data.setRespMsg("订单查询失败："+msg);
				PayLog.getLogger().info("微笑支付订单查询失败：" + msg);
			 }
		} catch (Exception e) {
			PayLog.getLogger().info("微笑支付订单查询异常：" + e.getMessage(), e);
			data.setCode("-1");
			data.setRespMsg("订单查询异常");
		}
		return data;
	}

}
