package com.hitler.service;

import com.hitler.entity.PayOrder;
import com.hitler.service.support.IGenericService;

/**
 * 在线充值记录 Service层
 * @author 
 *
 */
public interface IPayOrderService extends IGenericService<PayOrder, Integer> {
	
	/**
	 * 根据外部订单号查询充值订单
	 * @param billno
	 * @return
	 */
	public PayOrder queryOrderByTransBillno(String billno);
	/**
	 * 根据接入订单号查询充值订单
	 * @param billno
	 * @return
	 */
	public PayOrder queryOrderByConnBillno(String billno);
	
	/**
	 * 修改订单状态
	 * @param orderId
	 * @param status
	 * @param desc
	 * @return
	 */
	public boolean updateOrderStatus(PayOrder payOrder);

}
