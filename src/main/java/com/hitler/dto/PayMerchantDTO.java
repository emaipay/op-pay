package com.hitler.dto;

import com.hitler.core.dto.TransientDTO;
import com.hitler.entity.PayPlatform;

/**
 * 第三方商户Dto
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 下午5:35:29
 */
public class PayMerchantDTO extends TransientDTO<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1099215496072706361L;

	private Integer id;


	private String lastModifiedBy;

	private String lastModifiedDate;

	/**
	 * 是否可用
	 */
	private Boolean available;

	/**
	 * 卡上余额
	 */
	private Double currentBalance;

	/**
	 * 绑定的二级域名
	 */
	private String domainName;

	/**
	 * 手续费比例
	 */
	private Double feePercent;

	/**
	 * 密钥
	 */
	private String merchantKey;

	/**
	 * 商户名称
	 */
	private String merchantName;

	/**
	 * 商户号
	 */
	private String merchantNo;


	/**
	 * 终端号
	 */
	private String terminalNo;

	/**
	 * 预警金额
	 */
	private double warningAmount;

	/**
	 * 支付平台ID
	 */
	private PayPlatform platformId;

	/**
	 * 接入终端类型
	 */
	private String terminalType;
	
	private String paycode;

	public String getPaycode() {
		return paycode;
	}

	public void setPaycode(String paycode) {
		this.paycode = paycode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	
	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public double getFeePercent() {
		return feePercent;
	}

	public void setFeePercent(double feePercent) {
		this.feePercent = feePercent;
	}

	public String getMerchantKey() {
		return merchantKey;
	}

	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}



	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public double getWarningAmount() {
		return warningAmount;
	}

	public void setWarningAmount(double warningAmount) {
		this.warningAmount = warningAmount;
	}


	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public PayPlatform getPlatformId() {
		return platformId;
	}

	public void setPlatformId(PayPlatform platformId) {
		this.platformId = platformId;
	}

	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public void setFeePercent(Double feePercent) {
		this.feePercent = feePercent;
	}

	
 
}
