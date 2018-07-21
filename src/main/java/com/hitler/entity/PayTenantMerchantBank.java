package com.hitler.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hitler.core.entity.CheckableEntity;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_pay_tenant_merchant_bank")
public class PayTenantMerchantBank extends CheckableEntity<Integer> {

	private static final long serialVersionUID = -8061138425644872819L;
	@ManyToOne
	@JoinColumn(name = "pay_bank_id",referencedColumnName="id", columnDefinition = "int(11) comment '银行ID,对应tb_pay_bank'", nullable = false)
	private PayPlatformBank payBankId;
	@ManyToOne
	@JoinColumn(name = "pay_merchant_id",referencedColumnName="id",  columnDefinition = "int(11) comment '商户id,对应tb_pay_merchant'", nullable = false)
	private PayMerchant payMerchantId;
	@ManyToOne
	@JoinColumn(name = "TENANT_ID",referencedColumnName="id",  columnDefinition = "int(11) comment '接入平台ID'", nullable = false)
	private PayTenant payTenantId;

	



	

	public PayPlatformBank getPayBankId() {
		return payBankId;
	}

	public void setPayBankId(PayPlatformBank payBankId) {
		this.payBankId = payBankId;
	}

	public PayMerchant getPayMerchantId() {
		return payMerchantId;
	}

	public void setPayMerchantId(PayMerchant payMerchantId) {
		this.payMerchantId = payMerchantId;
	}

	public PayTenant getPayTenantId() {
		return payTenantId;
	}

	public void setPayTenantId(PayTenant payTenantId) {
		this.payTenantId = payTenantId;
	}

}
