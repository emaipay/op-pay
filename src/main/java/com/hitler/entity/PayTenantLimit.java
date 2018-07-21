package com.hitler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hitler.core.entity.CheckableEntity;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_pay_tenant_limit")
public class PayTenantLimit extends CheckableEntity<Integer> {

	private static final long serialVersionUID = -4415307744087657223L;
	@Column(name = "DAILY_RECHARGE_AMOUNT_MAX", columnDefinition = "DECIMAL(18,5) DEFAULT 0.0 COMMENT '每日最大充值金额'")
	private Double dailyRechargeAmountMax;
	@Column(name = "DAILY_RECHARGE_TIMES_MAX", columnDefinition = "INT DEFAULT 0 COMMENT '每日最大充值次数'")
	private Integer dailyRechargeTimesMax;
	@Column(name = "ONETIME_RECHARGE_AMOUNT_MAX", columnDefinition = "DECIMAL(18,5) DEFAULT 0.0 COMMENT '单笔最大充值金额'")
	private Double onetimeRechargeAmountMax;
	@Column(name = "ONETIME_RECHARGE_AMOUNT_MIN", columnDefinition = "DECIMAL(18,5) DEFAULT 0.0 COMMENT '单笔最小充值金额'")
	private Double onetimeRechargeAmountMin;
	@Column(name = "PLATFORM_ID", columnDefinition = "INT(11)  COMMENT '第三方支付商户ID'", nullable = false)
	private Integer platformId;
	@Column(name = "TENANT_ID", columnDefinition = "INT(11)  COMMENT '是哪个接入平台'", nullable = false)
	private Integer tenantId;
	@Column(name = "PLATFORM_Code", columnDefinition = "varchar(15)  COMMENT '第三方支付商户CODE'", nullable = false)
	private String platformCode;

	public Double getDailyRechargeAmountMax() {
		return dailyRechargeAmountMax;
	}

	public void setDailyRechargeAmountMax(Double dailyRechargeAmountMax) {
		this.dailyRechargeAmountMax = dailyRechargeAmountMax;
	}

	public Integer getDailyRechargeTimesMax() {
		return dailyRechargeTimesMax;
	}

	public void setDailyRechargeTimesMax(Integer dailyRechargeTimesMax) {
		this.dailyRechargeTimesMax = dailyRechargeTimesMax;
	}

	public Double getOnetimeRechargeAmountMax() {
		return onetimeRechargeAmountMax;
	}

	public void setOnetimeRechargeAmountMax(Double onetimeRechargeAmountMax) {
		this.onetimeRechargeAmountMax = onetimeRechargeAmountMax;
	}

	public Double getOnetimeRechargeAmountMin() {
		return onetimeRechargeAmountMin;
	}

	public void setOnetimeRechargeAmountMin(Double onetimeRechargeAmountMin) {
		this.onetimeRechargeAmountMin = onetimeRechargeAmountMin;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	

	public Integer getTenantId() {
		return tenantId;
	}

	public void setTenantId(Integer tenantId) {
		this.tenantId = tenantId;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

}
