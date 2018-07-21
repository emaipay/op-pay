package com.hitler.dto;

import com.hitler.core.dto.TransientDTO;
import com.hitler.entity.PayBank;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayTenant;

/**
 * 接入商户对应商户的支付方式(银行卡) Dto Thu May 04 10:06:49 CST 2017 klp
 */
public class PayTenantMerchantBankDTO extends TransientDTO<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 480808255819951998L;

	private Integer id;

	private String createdBy;

	private String createdDate;

	private String lastModifiedBy;

	private String lastModifiedDate;

	/**
	 * 银行ID,对应tb_pay_bank
	 */
	private PayBank payBankId;

	/**
	 * 接入平台ID
	 */
	private PayTenant payTenantId;

	/**
	 * 商户id,对应tb_pay_merchant
	 */
	private PayMerchant  payMerchantId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public PayBank getPayBankId() {
		return payBankId;
	}

	public void setPayBankId(PayBank payBankId) {
		this.payBankId = payBankId;
	}

 
	public PayTenant getPayTenantId() {
		return payTenantId;
	}

	public void setPayTenantId(PayTenant payTenantId) {
		this.payTenantId = payTenantId;
	}

	public PayMerchant getPayMerchantId() {
		return payMerchantId;
	}

	public void setPayMerchantId(PayMerchant payMerchantId) {
		this.payMerchantId = payMerchantId;
	}
	
	

 
}
