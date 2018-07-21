package com.hitler.dto;

import com.hitler.core.dto.TransientDTO;
import com.hitler.entity.PayPlatform;

/**
 * 第三方商户Dto
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 下午5:35:29
 */
public class PayMerchantCreateDTO extends TransientDTO<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4772129556345674166L;


	private Integer id;


	/**
	 * 是否可用
	 */
	private Boolean available=Boolean.TRUE;

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
	private String key;

	/**
	 * 商户名称
	 */
	private String merchantName;

	/**
	 * 商户号
	 */
	private String merchantNo;

	/**
	 * 商户公钥字段
	 */
	private String publicKey;

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
	
	private Integer pageType;	

	public Integer getPageType() {
		return pageType;
	}

	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	}

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

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public Double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public Double getFeePercent() {
		return feePercent;
	}

	public void setFeePercent(Double feePercent) {
		this.feePercent = feePercent;
	}

	

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
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

	public PayPlatform getPlatformId() {
		return platformId;
	}

	public void setPlatformId(PayPlatform platformId) {
		this.platformId = platformId;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	
 
}
