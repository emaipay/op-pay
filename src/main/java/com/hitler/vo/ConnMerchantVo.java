package com.hitler.vo;

import java.util.List;

/**
 * 用于返回查询商户跟商户对应银行的vo类
 * 
 * @author jt-x 2017-03-02 下午 15:31
 */
public class ConnMerchantVo {
	private Integer merchantId;// 商户号
	private String platformName;// 支付类型名称
	private Integer platformId;// 支付类型id
	private String platformLogo;// 支付类型图片地址
	private PayConnLimitVo payConnLimitVo;
	private List<MerchantBankVo> bankList;

	
	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
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

	public List<MerchantBankVo> getBankList() {
		return bankList;
	}

	public void setBankList(List<MerchantBankVo> bankList) {
		this.bankList = bankList;
	}

	public PayConnLimitVo getPayConnLimitVo() {
		return payConnLimitVo;
	}

	public void setPayConnLimitVo(PayConnLimitVo payConnLimitVo) {
		this.payConnLimitVo = payConnLimitVo;
	}

}
