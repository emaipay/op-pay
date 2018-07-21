package com.hitler.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayOrder;

public interface IPayOrderRepository extends GenericRepository<PayOrder, Integer> {
	
	/**
	 * 根据外部订单号查询充值订单
	 * @param billno
	 * @return
	 */
	@Query("from PayOrder where transBillNo=:transBillno")
	public PayOrder queryOrderByTransBillno(@Param("transBillno")String billno);
	
	
	/**
	 * 根据接入平台订单号查询充值订单
	 * @param billno
	 * @return
	 */
	@Query("from PayOrder where connBillno=:connBillno")
	public PayOrder queryOrderByConnBillno(@Param("connBillno")String billno);
	
	
	/**
	 * 修改订单状态
	 * @param orderId
	 * @param status
	 * @param desc
	 * @return
	 */
	@Modifying
	@Query(value="update t_pay_order po set po.ORDER_STATUS=:status,po.ORDER_STATUS_DESC=:statusDesc where po.id=:orderId ",nativeQuery=true)
	public int updateOrderStatus(@Param("orderId")Integer orderId,@Param("status")String status,@Param("statusDesc")String desc);


}
