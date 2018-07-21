package com.hitler.core.dto.pay;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class PayMerchantDto implements Serializable{
	
	private Integer merchantId;//	Y	int		支付商ID
	private String platformName;//	Y	String		平台名称
	private String platformLogo;//	Y	String		logo
	private List<BankDto> bankList;//	Y	List<Bank>		支付渠道列表
	
	public Integer getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public String getPlatformLogo() {
		return platformLogo;
	}
	public void setPlatformLogo(String platformLogo) {
		this.platformLogo = platformLogo;
	}
	public List<BankDto> getBankList() {
		return bankList;
	}
	public void setBankList(List<BankDto> bankList) {
		this.bankList = bankList;
	}

}
