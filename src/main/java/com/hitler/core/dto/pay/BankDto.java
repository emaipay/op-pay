package com.hitler.core.dto.pay;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BankDto implements Serializable{
	
	private Integer bankId;//	Y	int		支付通道ID
	private Integer merchantId;//	Y	int		支付商ID
	private String bankName;//	Y	String		支付通道名称
	private String bankLogo;//	Y	String		logo
	
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public Integer getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankLogo() {
		return bankLogo;
	}
	public void setBankLogo(String bankLogo) {
		this.bankLogo = bankLogo;
	}

}
