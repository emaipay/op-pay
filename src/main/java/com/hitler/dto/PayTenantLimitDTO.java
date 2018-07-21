package com.hitler.dto;

import com.hitler.core.dto.TransientDTO;

/**
 * 接入商户第三方支付限额 Dto Thu May 04 09:30:12 CST 2017 klp
 */
public class PayTenantLimitDTO extends TransientDTO<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4627702625502685506L;

	private Integer id;

	private String createdBy;

	private String createdDate;

	private String lastModifiedBy;

	private String lastModifiedDate;

	/**
	 * 是哪个接入平台
	 */
	private Integer tenantId;

	/**
	 * 每日最大充值金额
	 */
	private double dailyRechargeAmountMax;

	/**
	 * 每日最大充值次数
	 */
	private Integer dailyRechargeTimesMax;

	/**
	 * 单笔最大充值金额
	 */
	private double onetimeRechargeAmountMax;

	/**
	 * 单笔最小充值金额
	 */
	private double onetimeRechargeAmountMin;

	/**
	 * 第三方支付商户CODE
	 */
	private String platformCode;

	/**
	 * 第三方支付商户ID
	 */
	private Integer platformId;

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

	public Integer getTenantId() {
		return tenantId;
	}

	public void setTenantId(Integer tenantId) {
		this.tenantId = tenantId;
	}

	public double getDailyRechargeAmountMax() {
		return dailyRechargeAmountMax;
	}

	public void setDailyRechargeAmountMax(double dailyRechargeAmountMax) {
		this.dailyRechargeAmountMax = dailyRechargeAmountMax;
	}

	public Integer getDailyRechargeTimesMax() {
		return dailyRechargeTimesMax;
	}

	public void setDailyRechargeTimesMax(Integer dailyRechargeTimesMax) {
		this.dailyRechargeTimesMax = dailyRechargeTimesMax;
	}

	public double getOnetimeRechargeAmountMax() {
		return onetimeRechargeAmountMax;
	}

	public void setOnetimeRechargeAmountMax(double onetimeRechargeAmountMax) {
		this.onetimeRechargeAmountMax = onetimeRechargeAmountMax;
	}

	public double getOnetimeRechargeAmountMin() {
		return onetimeRechargeAmountMin;
	}

	public void setOnetimeRechargeAmountMin(double onetimeRechargeAmountMin) {
		this.onetimeRechargeAmountMin = onetimeRechargeAmountMin;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

}
