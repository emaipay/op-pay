package com.hitler.dto;

import com.hitler.core.dto.TransientDTO;
import com.hitler.entity.PayBank;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayOrder.OrderStatus;
import com.hitler.entity.PayPlatform;
import com.hitler.entity.PayTenant;

public class PayOrderDto extends TransientDTO<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5619791329765686767L;
	
	private Integer id;
	/**
	 * 订单号
	 */
	private String billNo ;

	/**
	 * 第三方支付单号
	 */
	private String transBillNo;

	/**
	 * 商户ID
	 */
	private PayMerchant merchantId;

	/**
	 * 订单金额
	 */
	private Double orderAmount;
	/**
	 * 实际金额
	 */
	private Double factAmount = 0D;
	/**
	 * 手续费
	 */
	private Double fee = 0D;

	/**
	 * 付款银行
	 */
	private PayBank payerBankId;

	/**
	 * 订单状态
	 */
	private OrderStatus orderStatus;

	/**
	 * 支付结果描述
	 */
	private String orderStatusDesc;
	/**
	 * 支付类型
	 */
	private PayPlatform platformId;
	private PayTenant tenantId;
	private String connBillno ;
	
	private String lastModifiedDate;
	private String createdDate;
	
	
	
	

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getConnBillno() {
		return connBillno;
	}

	public void setConnBillno(String connBillno) {
		this.connBillno = connBillno;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getTransBillNo() {
		return transBillNo;
	}

	public void setTransBillNo(String transBillNo) {
		this.transBillNo = transBillNo;
	}

	public PayMerchant getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(PayMerchant merchantId) {
		this.merchantId = merchantId;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Double getFactAmount() {
		return factAmount;
	}

	public void setFactAmount(Double factAmount) {
		this.factAmount = factAmount;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public PayBank getPayerBankId() {
		return payerBankId;
	}

	public void setPayerBankId(PayBank payerBankId) {
		this.payerBankId = payerBankId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}

	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}

	public PayPlatform getPlatformId() {
		return platformId;
	}

	public void setPlatformId(PayPlatform platformId) {
		this.platformId = platformId;
	}

	public PayTenant getTenantId() {
		return tenantId;
	}

	public void setTenantId(PayTenant tenantId) {
		this.tenantId = tenantId;
	}

}
