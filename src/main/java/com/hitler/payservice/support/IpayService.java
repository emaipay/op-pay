package com.hitler.payservice.support;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hitler.entity.PayOrder;

public interface IpayService {

	/**
	 * 获取支付所需要的数据
	 * 
	 * @param request
	 * @param model
	 * @param order
	 * @return
	 */
	Map<String, Object> getPayData(HttpServletRequest request, PayOrder order,String payCode);

	/**
	 * 获取回调的支付订单号
	 * 
	 * @param request
	 * @return
	 */
	String getPayOrderNo(HttpServletRequest request);

	/**
	 * 校验回调的数据
	 * 
	 * @param request
	 * @param order
	 */
	void checkPayBackData(HttpServletRequest request, PayOrder order) throws Exception;

	/**
	 * 查询订单
	 * 
	 * @param order
	 * @return
	 */
	PayOrderQueryData queryOrder(HttpServletRequest request,PayOrder order)  throws Exception;
	

}
