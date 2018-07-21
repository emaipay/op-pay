package com.hitler.payservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.hitler.core.log.PayLog;
import com.hitler.core.utils.DateUtil;
import com.hitler.core.utils.HttpUtils;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayMerchant.TerminalType;
import com.hitler.entity.PayOrder;
import com.hitler.entity.PayPlatform;
import com.hitler.payservice.platform.rh.ConfigInfo;
import com.hitler.payservice.platform.rh.ExcuteRequestUtil;
import com.hitler.payservice.platform.rh.InitUtil;
import com.hitler.payservice.support.IpayService;
import com.hitler.payservice.support.PayOrderQueryData;
import com.lechinepay.channel.lepay.client.apppay.AppPay;
import com.lechinepay.channel.lepay.share.LePayParameters;

/**
 * 融合支付
 * @author xu
 *
 */
public class RhPayService implements IpayService {
	
	public static final String CALLBACK_PAGE_PATH = "/lfb/callback-page";
	public static final String CALLBACK_DATA_PATH = "/lfb/callback-data";
	public static final String API_VERSION = "1.0.0";
	public static final String TEST_PAGEBACK="http://183.238.56.249:8017/rh_return.jsp";
	@Override
	public Map<String, Object> getPayData(HttpServletRequest request, PayOrder order, String payCode) {
		PayMerchant pm = order.getMerchantId();
		PayPlatform pp = order.getPlatformId();
		InitUtil.init(pm.getKey());
		String requestUrl = request.getScheme() + "://" + request.getServerName();
		String callbackPage = requestUrl + "/rh_return.jsp";
		/*** 组装请求报文 ***/
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put(LePayParameters.VERSION, API_VERSION);
		requestMap.put(LePayParameters.ENCODING, "UTF-8");
		requestMap.put(LePayParameters.SIGNATURE, "");
		requestMap.put(LePayParameters.REQ_RESERVED, order.getBillNo().substring(4));// 透传字段,自定义传的参数
		requestMap.put(LePayParameters.MCH_ID, pm.getMerchantNo());
		requestMap.put(LePayParameters.CMP_APP_ID, pm.getTerminalNo());
		requestMap.put(LePayParameters.PAY_TYPE_CODE, payCode);
		requestMap.put(LePayParameters.OUT_TRADE_NO, order.getBillNo().substring(4));
		requestMap.put(LePayParameters.TRADE_TIME, DateUtil.date2Str(order.getCreatedDate()));
		requestMap.put(LePayParameters.AMOUNT, Math.round(order.getOrderAmount()*100));
		requestMap.put(LePayParameters.SUMMARY, "LfbPay");
		requestMap.put(LePayParameters.SUMMARY_DETAIL, "recharge");
		requestMap.put(LePayParameters.DEVICE_ID, "PC");
		requestMap.put(LePayParameters.DEVICE_IP, HttpUtils.getAddr(request));
		requestMap.put(LePayParameters.RETURN_URL, callbackPage+"#"+order.getBillNo().substring(4));
		PayLog.getLogger().info("[融合:{},{}]表单数据:{}",order.getTransBillNo(),order.getConnBillno(),JSON.toJSONString(requestMap));
		try {
			Map<String, Object> responseMap = ExcuteRequestUtil.excute(pp.getPayUrl(), ConfigInfo.short_url_pay, requestMap);
			Object webOrderInfo =null;
			if(order.getTerminalType().getValue().equals(TerminalType.手机)){
				webOrderInfo = responseMap.get("h5OrderInfo");
			}else{
				webOrderInfo = responseMap.get("webOrderInfo");
			}
			requestMap.clear();
			if("ym.pay".equals(payCode)){
				requestMap.put("qrCode", true);
			}
			requestMap.put("postUrl", webOrderInfo);
			return requestMap;
		} catch (Exception e) {
			e.printStackTrace();
			PayLog.getLogger().info("[融合:{},{}]:表单数据{}",order.getTransBillNo(),order.getConnBillno(),JSON.toJSONString(requestMap), e);
		}
		return null;
	}
	

	@Override
	public String getPayOrderNo(HttpServletRequest request) {
		String outTradeNo=request.getParameter("outTradeNo");
        PayLog.getLogger().info("[融合:{}]回调订单号", outTradeNo);
		return outTradeNo;
	}

	@Override
	public void checkPayBackData(HttpServletRequest request, PayOrder order)  throws Exception{
		PayLog.getLogger().info("[融合:{},{}]开始检查数据", order.getTransBillNo(),order.getConnBillno());
		Map<String, Object> params = new HashMap<String, Object>();
		PayMerchant pm=order.getMerchantId();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        if (!AppPay.verify(params)) {// 验证成功
        	 throw new Exception("融合支付回调保存失败,数据来源校验失败");
        } 
        Double amount=Double.parseDouble(request.getParameter("amount"));
        if((amount)/100!=order.getOrderAmount()){
        	throw new Exception("融合支付回调保存失败,回调金额不一致");
        }
        String merId=request.getParameter("mchId");
        if(!pm.getMerchantNo().equals(merId)){
        	throw new Exception("融合支付回调保存失败,回调商户不一致");
        }
        order.setFactAmount(amount/100);
        PayLog.getLogger().info("[融合:{},{}]检查数据成功", order.getTransBillNo(),order.getConnBillno());
	}

	@Override
	public PayOrderQueryData queryOrder(HttpServletRequest request,PayOrder order)  throws Exception{
		PayLog.getLogger().info("[融合:{},{}]开始查询订单", order.getTransBillNo(),order.getConnBillno());
		PayMerchant pm=order.getMerchantId();
		// 初始化生产环境测试秘钥
        InitUtil.init(pm.getKey());
        PayOrderQueryData data = new PayOrderQueryData();
        data.setCode("-1");
        /*** 组装请求报文 ***/
        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put(LePayParameters.VERSION, "1.0.0");
        requestMap.put(LePayParameters.ENCODING, "UTF-8");
        requestMap.put(LePayParameters.MCH_ID, pm.getMerchantNo());
        requestMap.put(LePayParameters.CMP_APP_ID, pm.getTerminalNo());
        requestMap.put(LePayParameters.PAY_TYPE_CODE, "web.pay");
        requestMap.put(LePayParameters.OUT_TRADE_NO, order.getBillNo().substring(4));
        requestMap.put(LePayParameters.DEVICE_IP, HttpUtils.getAddr(request));
        // 生产环境交易地址
        String url = "https://openapi.unionpay95516.cc/pre.lepay.api";

        // 测试环境交易地址
        // String url = "https://lepay.asuscomm.com/pre.lepay.api";
        Map<String, Object> responseMap = ExcuteRequestUtil.excute(url, ConfigInfo.short_url_query, requestMap);
        PayLog.getLogger().info("[融合:{},{}]返回结果", order.getTransBillNo(),order.getConnBillno(),JSON.toJSONString(responseMap));
        if(!AppPay.verify(responseMap)){
        	data.setRespMsg("融合支付回调保存失败,数据来源校验失败");
        }
        String tradStatus=responseMap.get("tradeStatus").toString();
        if(!"2".equals(tradStatus)){
        	if("-1".equals(tradStatus)){
        		data.setRespMsg("异常");
        		return data;
        	}else if("1".equals(tradStatus)){
        		data.setRespMsg("未支付");
        		return data;
        	}else if("3".equals(tradStatus)){
        		data.setRespMsg("待支付");
        		return data;
        	}else if("4".equals(tradStatus)){
        		data.setRespMsg("完成");
        		return data;
        	}else if("5".equals(tradStatus)){
        		data.setRespMsg("取消");
        		return data;
        	}else if("6".equals(tradStatus)){
        		data.setRespMsg("驳回");
        		return data;
        	}else if("7".equals(tradStatus)){
        		data.setRespMsg("处理中");
        		return data;
        	}else if("8".equals(tradStatus)){
        		data.setRespMsg("关闭");
        		return data;
        	}else if("9".equals(tradStatus)){
        		data.setRespMsg("支付失败");
        		return data;
        	}else if("10".equals(tradStatus)){
        		data.setRespMsg("不可支付");
        		return data;
        	}
        }
        data.setAmount(Double.parseDouble(responseMap.get("amount").toString())/100);
		data.setTradeTime(DateUtil.str2Date(responseMap.get("tradeTime").toString()));
		PayLog.getLogger().info("[融合:{},{}]查询订单成功", order.getTransBillNo(),order.getConnBillno());
		data.setCode("0");
        return data;
	}

}
