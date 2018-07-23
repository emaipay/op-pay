package com.hitler.payservice;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.hitler.core.Constants;
import com.hitler.core.dto.ResultDTO;
import com.hitler.core.exception.ThirdPayBussinessException;
import com.hitler.core.log.PayLog;
import com.hitler.core.utils.DateUtil;
import com.hitler.core.utils.StringUtils;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayOrder;
import com.hitler.entity.PayPlatform;
import com.hitler.payservice.support.IpayService;
import com.hitler.payservice.support.PayOrderQueryData;

public class AliPayService implements IpayService {
	
	public static final String CALLBACK_PAGE_PATH = "/ali/callback-page";
	public static final String CALLBACK_DATA_PATH = "/ali/callback-data";

	@Override
	public Map<String, Object> getPayData(HttpServletRequest request,
			PayOrder order, String payCode) {
		Map<String, Object> respMap = new HashMap<String, Object>();
		respMap.put("redirect", 0);
		if(payCode.equals("QUICK_WAP_PAY")){//AliPayApi wapPay
			respMap.put("postUrl", wapPay(request, order, payCode));
			return respMap;
		}else{//AliPayApi appPay
			String params = appPay(request, order, payCode);
			respMap.put("postUrl", params);
			return respMap;
		}
		
	}
	
	private String appPay(HttpServletRequest httpRequest, PayOrder order, String payCode){
		PayMerchant pm = order.getMerchantId();
		PayPlatform pp = order.getPlatformId();
		//String call = httpRequest.getScheme() + "://" + httpRequest.getServerName();
		String call = "http://pay.woyao518.com";
		String notifyUrl = call + "/pay/callback" + CALLBACK_DATA_PATH;
		PayLog.getLogger().info("notifyUrl:"+notifyUrl);
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody("商城");
		model.setSubject("商城充值");
		model.setOutTradeNo(order.getTransBillNo());
		model.setTimeoutExpress("30m");
		model.setTotalAmount(order.getOrderAmount()+ "");
		//"QUICK_MSECURITY_PAY"
		model.setProductCode(payCode);
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(pp.getPayUrl(),
					pm.getMerchantNo(),  pm.getKey(), "json","UTF-8", pm.getPublicKey(), "RSA2");
			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
			Map<String, Object> respMap = new HashMap<String, Object>();
			respMap.put("success", true);//操作结果,true-成功;false-失败
			respMap.put("respCode", Constants.SUCCESS);//返回编码
			respMap.put("respMsg", "成功");//返回消息
			respMap.put("params", response.getBody());//App调起支付所需参数
			return JSON.toJSONString(respMap);
		} catch (AlipayApiException e) {
			PayLog.getLogger().error("AliPayApi appPay AlipayApiException:"+e.getMessage(), e);
			//return e.getMessage();
			return JSON.toJSONString(ResultDTO.error(Constants.PAY_FAIL, e.getMessage()));
		}
	}
	
	public static void main(String[] args) throws AlipayApiException {


		//RSA签名方式调用：AlipaySignature.rsaSign(params, privateKey, charset);
		//RSA2签名方式调用：
		String privateKey = "MIIEuwIBADANBgkqhkiG9w0BAQEFAASCBKUwggShAgEAAoIBAQCoAgMfinRkBeFhOP8AaR6O9huizd1IQeHfqQtJIfQL0OgCJKGXhDc97mdPQqm+IGUTMjOocD25V9DJ0H5x4AvpTtCr+1mIHkEWYPDLi0UbMbc6KAPQAJzYm3KKqSR64xb+y+MIE+9CB0At7scz68YvA4J+hd3fc8NcF4cW3OIxUju74XS+hTc9XCob0/y2CXqRIhjg0+AITJuIuTReOg/kZTVuoE1ZA2puhcRJgFzNykDFnQzVy7lPaVzZyHSnfdiF9pHv2lhzlnBcV6MTgyJyBxV5AFpmTouZoaWwMEYB6DLzahhtEPWWDzJYDQ2AfLZvXLaMuOCynDDbM9jUgIAhAgMBAAECgf8x4GcM9g2XpCdEtab+eDaaOnX53yPST7y+qXCwRWQuHdbCzB0s3hOITz5cSW4bA4L+L+kmGCw62dciEsN3Cwx+Nue8JuJtho3MFvtlcfMSH7BFk5uOJKBrrbpTb+NkmAY7nkvIGiLtdzmWaUy6HUS4sFINPbCgpb1ZmaKBJxH/ke6qrrU8mcMxLw/UGVITWbMs5QlonM+3HL2V/rF69fqs+UI9JRWnNfvCx4j0Xk7Te8EwuQgjF8WHc3Rdw8S2+8zQakB6UzwkSFmtIpDSSAtJ8jF14cClEeErZ8x3DgukDpItuWbw6612nRS6AdY4DeexDGtPD68uKX15MBzvA/ECgYEA39wBX6Udv8jedj+dLuwQrgsyzHDNKaKPTuCXw7GdcCT8YNmci8xu12F2dPORUkaeD79NCtC25LmVKYaFVyctlWIYVLIBEOzj6bEadv0P26n88Gk6aKLJClkQjlhMGoB8bqZP5DcBrE5I9G62Cp8JBmzI+TFjd/N8JiMnStoELXsCgYEAwCEsS0QneCcyveyDxnbem5gkepLkSYDxqJ7qmtzvSWSe8i6aPtxtYJ6zXbSTNamPSbPcu8FLCGKNhMhrFSYzlKnhYqex021gBs6oJX9i4y0gGZZB28d9AHIiCk5HgL21KI957poGdyUb/MP9iNsU9Dv/J/FLU7jn44aba+J5YBMCgYBk+v5XockQqVDIsJt7/hrsF7sbg8rYfC7/Gsr7CNUHMHf0TFWUrmxVle50hSbJoXdNjTL4rPvDo3+I0ti1XsPuPejR09YtR17sHOreP/t2UHD8Fcu2RbBAjbqtK2FxFwKKe1VQkgucKVg5gvN1SRYuIsZpuoCvLFOVdIgV+NCnnwKBgGKv52+Vx2xJPiAeH6XOoxNq97naP9adnAOP9VOsjmC1hYtCcso9MRWDDwDf4wmaWdZyw7vRUngxHx4IUDCh2bbP98xT9Nxz/vIgLnBsWV/1atqZuqRoeIXFGkUO5l1zlF0MLYZoqU+DyTdxeIZWehHly2852wZbIQlWt/iXcN7vAoGBAJcNlQ6GVCYpB2gttNNCPN0igxYnEiYWQJ5aVrybtbzWA9dSetMcOmLA924yHzZRY7CfI1lvveX0QkU+rt5sZnreoGLzcBX0bWgEirceF/QCQdvyz0SNGoGZ94JMgJzn7sAi+UrwCHDl9pHhjDa7S8mMOSNdBzgQnb5vc08kX5fP";
		Map<String ,String> params = new HashMap<String, String>();
		params.put("gmt_create", "2017-12-29 18:47:54");
		params.put("charset", "UTF-8");
		params.put("seller_email", "mozhilieqiu_wxgzh@jjcai.net");
		params.put("subject", "超神商城充值");
		//params.put("sign", "rqPJ7SU1trrWuRLHPRUaqveh/gWDIebf/hdqv8jxMd0MJ/X8LN9gUMJeTuzomVLVN1kuKwR0qsejwOoS4LqaYugwbVbNqj2VyZhbx4MSE4EzgLdFo4Pkojo260jFFSaZmQUk2hePUbgENNFlBPxRAisn1GXgFg5dC6MxNXTsP33PX2xtUvJHjmeCSTNGoEYVzV321TVo4gkCyPntKnOi3FxSQOOfC1Il3oItVpVcnJst9iXchj03fovxhxTN8KCHTGzLB0MT8BhyVsNbsDPbnW3rkSb0OpQkD28uJGCzB6OnxItXJ2IEqwPLnp8kvgFdwpIFczMjXeGN8+vxDeqcyQ==");
		params.put("body", "魔智猎球商城");
		params.put("buyer_id", "2088202906200429");
		params.put("invoice_amount", "0.10");
		params.put("notify_id", "29da3c11ed4fd88528d2a9b574db908j8q");
		params.put("fund_bill_list", "[{\"amount\":\"0.10\",\"fundChannel\":\"ALIPAYACCOUNT\"}]");
		params.put("notify_type", "trade_status_sync");
		params.put("trade_status", "TRADE_SUCCESS");
		params.put("receipt_amount", "0.10");
		params.put("app_id", "2017103009620393");
		params.put("buyer_pay_amount", "0.10");
		//params.put("sign_type", "RSA2");
		params.put("seller_id", "2088821559121929");
		params.put("gmt_payment", "2017-12-29 18:47:55");
		params.put("notify_time", "2017-12-29 18:47:56");
		params.put("version", "1.0");
		params.put("out_trade_no", "171229184653030484");
		params.put("total_amount", "0.10");
		params.put("trade_no", "2017122921001004420269087939");
		params.put("auth_app_id", "2017103009620393");
		params.put("buyer_logon_id", "cyw***@yeah.net");
		params.put("point_amount", "0.00");
		String sign = AlipaySignature.rsa256Sign(AlipaySignature.getSignContent(params), privateKey, "UTF-8");
		System.out.println(sign);
		params.put("sign", sign);
		//AlipaySignature.rsa256CheckContent(arg0, arg1, arg2, arg3);
		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqAIDH4p0ZAXhYTj/AGkejvYbos3dSEHh36kLSSH0C9DoAiShl4Q3Pe5nT0KpviBlEzIzqHA9uVfQydB+ceAL6U7Qq/tZiB5BFmDwy4tFGzG3OigD0ACc2JtyiqkkeuMW/svjCBPvQgdALe7HM+vGLwOCfoXd33PDXBeHFtziMVI7u+F0voU3PVwqG9P8tgl6kSIY4NPgCEybiLk0XjoP5GU1bqBNWQNqboXESYBczcpAxZ0M1cu5T2lc2ch0p33YhfaR79pYc5ZwXFejE4MicgcVeQBaZk6LmaGlsDBGAegy82oYbRD1lg8yWA0NgHy2b1y2jLjgspww2zPY1ICAIQIDAQAB";
		//String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqAIDH4p0ZAXhYTj/AGkejvYbos3dSEHh36kLSSH0C9DoAiShl4Q3Pe5nT0KpviBlEzIzqHA9uVfQydB+ceAL6U7Qq/tZiB5BFmDwy4tFGzG3OigD0ACc2JtyiqkkeuMW/svjCBPvQgdALe7HM+vGLwOCfoXd33PDXBeHFtziMVI7u+F0voU3PVwqG9P8tgl6kSIY4NPgCEybiLk0XjoP5GU1bqBNWQNqboXESYBczcpAxZ0M1cu5T2lc2ch0p33YhfaR79pYc5ZwXFejE4MicgcVeQBaZk6LmaGlsDBGAegy82oYbRD1lg8yWA0NgHy2b1y2jLjgspww2zPY1ICAIQIDAQAB";
		boolean verify_result = AlipaySignature.rsaCheckV1(params, publicKey, "UTF-8", "RSA2");
		System.out.println(verify_result);
	}
	
	private String wapPay(HttpServletRequest request, PayOrder order, String payCode){
		//String call = request.getScheme() + "://" + request.getServerName();
		String call = "http://pay.woyao518.com";
		String notifyUrl = call + "/pay/callback" + CALLBACK_DATA_PATH;
		PayLog.getLogger().info("notifyUrl:"+notifyUrl);
		String retUrl = call + "/pay/callback" + CALLBACK_PAGE_PATH;
		PayLog.getLogger().info("retUrl:"+retUrl);
		PayMerchant pm = order.getMerchantId();
		PayPlatform pp = order.getPlatformId();
		// 商户订单号，商户网站订单系统中唯一订单号，必填
	    String out_trade_no = order.getTransBillNo();
		// 订单名称，必填
	    String subject = "商城充值";
		//System.out.println(subject);
	    // 付款金额，必填
	    String total_amount = order.getOrderAmount()+ "";
	    // 商品描述，可空
	    //String body = new String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");
	    String body = "商城";
	    // 超时时间 可空
	   String timeout_express="30m";
	    
	    /**********************/
	    // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签     
	    //调用RSA签名方式
	    AlipayClient client = new DefaultAlipayClient(pp.getPayUrl(), pm.getMerchantNo(), 
	    		 pm.getKey(), "json","UTF-8", pm.getPublicKey(), "RSA2");
	    AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();
	    
	    // 封装请求支付信息
	    AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
	    model.setOutTradeNo(out_trade_no);
	    model.setSubject(subject);
	    model.setTotalAmount(total_amount);
	    model.setBody(body);
	    model.setTimeoutExpress(timeout_express);
	   // String product_code="QUICK_WAP_PAY";
	    model.setProductCode(payCode);
	    alipay_request.setBizModel(model);
	    // 设置异步通知地址
	    alipay_request.setNotifyUrl(notifyUrl); 
	    // 设置同步地址
	    alipay_request.setReturnUrl(retUrl);   
	    
	    // form表单生产
	    String form = "";
		try {
			// 调用SDK生成表单
			form = client.pageExecute(alipay_request).getBody();
			PayLog.getLogger().info("AliPayApi wapPay form:"+form);
			return form;
		} catch (AlipayApiException e) {
			PayLog.getLogger().error("AliPayApi wapPay AlipayApiException:"+e.getMessage(), e);
		} 
		
		return null;
	}
	

	@Override
	public String getPayOrderNo(HttpServletRequest request) {
		PayLog.getLogger().info("[支付宝]充值页面回调开始");
		String orderNo = request.getParameter("out_trade_no");
		/*if(StringUtils.isBlank(orderNo)){
			String json = getJsonContent(request);
			PayLog.getLogger().info("[云聚付]充值页面回调json:{}", json);
			YjfResponeData data = JsonUtil.convertToObject(json, YjfResponeData.class);
			orderNumber = data.getOrder_number();
		}*/
		PayLog.getLogger().info("[支付宝:{}]充值回调订单号", orderNo);
		return orderNo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void checkPayBackData(HttpServletRequest request, PayOrder order)
			throws Exception {
		
			PayMerchant pm = order.getMerchantId();
			//获取支付宝POST过来反馈信息
				Map<String,String> params = new HashMap<String,String>();
				Map requestParams = request.getParameterMap();
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
					if("sign_type".equals(name)){
						PayLog.getLogger().info("alipay_VVV_sign_type______"+name+":"+valueStr);
						continue;
					}
					params.put(name, valueStr);
					PayLog.getLogger().info("alipay_params______"+name+":"+valueStr);
				}
				//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
					//商户订单号
					try {
						String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
						PayLog.getLogger().info("alipay______out_trade_no:"+out_trade_no);
						//支付宝交易号

						String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
						PayLog.getLogger().info("alipay______trade_no:"+trade_no);

						//交易状态
						String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
						PayLog.getLogger().info("alipay______trade_status:"+trade_status);
						
						//buyer_id 	买家支付宝用户号 	String(16) 	否 	买家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字 	2088102122524333
						String buyer_id = new String(request.getParameter("buyer_id").getBytes("ISO-8859-1"),"UTF-8");
						PayLog.getLogger().info("alipay______buyer_id:"+buyer_id);
						
						//buyer_logon_id 	买家支付宝账号 	String(100) 	否 	买家支付宝账号 	15901825620
						String buyer_logon_id = new String(request.getParameter("buyer_logon_id").getBytes("ISO-8859-1"),"UTF-8");
						PayLog.getLogger().info("alipay______buyer_logon_id:"+buyer_logon_id);
						
						//seller_id 	卖家支付宝用户号 	String(30) 	否 	卖家支付宝用户号 	2088101106499364
						String seller_id = new String(request.getParameter("seller_id").getBytes("ISO-8859-1"),"UTF-8");
						PayLog.getLogger().info("alipay______seller_id:"+seller_id);
						
						//total_amount 	订单金额 	Number(9,2) 	否 	本次交易支付的订单金额，单位为人民币（元） 	20
						String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
						PayLog.getLogger().info("alipay______total_amount:"+total_amount);
						

						//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
						//计算得出通知验证结果
						//boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
						boolean verify_result = AlipaySignature.rsaCheckV1(params, pm.getPublicKey(), "UTF-8", "RSA2");
						
						PayLog.getLogger().info("alipay______verify_result:"+verify_result);
						
						if(verify_result){//验证成功
							//////////////////////////////////////////////////////////////////////////////////////////
							//请在这里加上商户的业务逻辑程序代码

							//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
							
							if(trade_status.equals("TRADE_FINISHED")){
								//判断该笔订单是否在商户网站中已经做过处理
									//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
									//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
									//如果有做过处理，不执行商户的业务程序
									
								//注意：
								//如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
								//如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
								//handleNotify(Long.parseLong(out_trade_no), trade_no, buyer_id, seller_id, total_amount);
								order.setTransactionId(trade_no);
								return;
							} else if (trade_status.equals("TRADE_SUCCESS")){
								//判断该笔订单是否在商户网站中已经做过处理
									//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
									//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
									//如果有做过处理，不执行商户的业务程序
									
								//注意：
								//如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
								//handleNotify(Long.parseLong(out_trade_no), trade_no, buyer_id, seller_id, total_amount);
								order.setTransactionId(trade_no);
								return;
							}

							//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
							//response.getWriter().clear();
							//out.println("success");	//请不要修改或删除
							
							//response.getWriter().write("success");
							
							PayLog.getLogger().error("[支付宝:{},{}]回调失败", order.getTransBillNo(), order.getConnBillno());
							//////////////////////////////////////////////////////////////////////////////////////////
						}else{//验证失败
							//response.getWriter().write("fail");
							PayLog.getLogger().error("[支付宝:{},{}]回调验证失败", order.getTransBillNo(), order.getConnBillno());
							throw new ThirdPayBussinessException("支付宝回调验证失败");
						}
						
						/*//直接将完整的表单html输出到页面 
						response.getWriter().flush(); 
						response.getWriter().close();*/
					} catch (UnsupportedEncodingException e) {
						PayLog.getLogger().error("Alipay notifyResult UnsupportedEncodingException:"+e.getMessage(), e);
					} catch (AlipayApiException e) {
						PayLog.getLogger().error("Alipay notifyResult AlipayApiException:"+e.getMessage(), e);
					} catch (Exception e) {
						PayLog.getLogger().error("Alipay notifyResult Exception:"+e.getMessage(), e);
					}
					
					throw new ThirdPayBussinessException("支付宝回调失败");
	}

	@Override
	public PayOrderQueryData queryOrder(HttpServletRequest request,
			PayOrder order) throws Exception {
		
		PayOrderQueryData data = new PayOrderQueryData();
		/*out_trade_no 	String 	特殊可选 	64 	订单支付时传入的商户订单号,和支付宝交易号不能同时为空。
		trade_no,out_trade_no如果同时存在优先取trade_no 	20150320010101001
		trade_no 	String 	特殊可选 	64 	支付宝交易号，和商户订单号不能同时为空 	2014112611001004680 073956707 */
		PayMerchant pm = order.getMerchantId();
		PayPlatform pp = order.getPlatformId();
		 AlipayClient client = new DefaultAlipayClient(pp.getPayUrl(), pm.getMerchantNo(), 
	    		 pm.getKey(), "json","UTF-8", pm.getPublicKey(), "RSA2");
	    AlipayTradeQueryRequest alipay_request=new AlipayTradeQueryRequest();
	 // 封装请求支付信息
	    AlipayTradeQueryModel model=new AlipayTradeQueryModel();
	    if(StringUtils.isNotBlank(order.getTransactionId())){
	    	  model.setTradeNo(order.getTransactionId());
	    }else{
	    	model.setOutTradeNo(order.getTransBillNo());
	    }
	    alipay_request.setBizModel(model);
	    try {
			AlipayTradeQueryResponse alipayResponse = client.execute(alipay_request);
			if (alipayResponse.isSuccess()) {
			    // 调用成功，则处理业务逻辑
			    if ("10000".equals(alipayResponse.getCode())) {
			        // 订单创建成功
			      //  交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款） 
			        PayLog.getLogger().info("支付宝订单查询结果：" + alipayResponse.getTradeStatus());
			        if(alipayResponse.getTradeStatus().equals("TRADE_FINISHED") || 
			        		alipayResponse.getTradeStatus().equals("TRADE_SUCCESS")){
			        	if (order.getOrderStatus().equals(PayOrder.OrderStatus.支付成功) == false) {
							// 检查回调信息
							order.setOrderStatus(PayOrder.OrderStatus.支付成功);
							order.setOrderStatusDesc(DateUtil.getNow() + ":" + PayOrder.OrderStatus.支付成功.getName());
						}
			        	data.setCode("0");
						data.setAmount(order.getFactAmount());
						data.setTradeTime(order.getCreatedDate());
						data.setRespMsg("支付成功");
			        }else if(alipayResponse.getTradeStatus().equals("TRADE_CLOSED")){
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
			    } else {
			    	PayLog.getLogger().info("支付宝订单查询失败：" + alipayResponse.getMsg() + ":" + alipayResponse.getSubMsg());
			    	data.setCode("-1");
					data.setRespMsg("订单查询失败");
			    }
			}
		} catch (AlipayApiException e) {
			PayLog.getLogger().info("支付宝订单查询异常：" + e.getMessage(), e);
			data.setCode("-1");
			data.setRespMsg("订单查询异常");
		}
	    
		return data;
	}

}
