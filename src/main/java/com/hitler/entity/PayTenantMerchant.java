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
@Table(name = "t_pay_tenant_merchant")
public class PayTenantMerchant extends CheckableEntity<Integer> {

	private static final long serialVersionUID = 2935423702968556171L;
	@ManyToOne
	@JoinColumn(name = "pay_merchant_id", columnDefinition = "INT COMMENT '商户表id'", nullable = false)
	private PayMerchant payMerchantId;
	@ManyToOne
	@JoinColumn(name = "TENANT_ID", columnDefinition = "INT(11) COMMENT '接入平台id'", nullable = false)
	private PayTenant tenantId;

	public PayMerchant getPayMerchantId() {
		return payMerchantId;
	}

	public void setPayMerchantId(PayMerchant payMerchantId) {
		this.payMerchantId = payMerchantId;
	}

	public PayTenant getTenantId() {
		return tenantId;
	}

	public void setTenantId(PayTenant tenantId) {
		this.tenantId = tenantId;
	}

	

}
