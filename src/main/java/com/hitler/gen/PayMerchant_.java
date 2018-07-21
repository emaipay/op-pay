package com.hitler.gen;


public enum PayMerchant_ {

	createdBy,
	createdDate,
	lastModifiedBy,
	lastModifiedDate,
	id,
	terminalNo,	// '终端号'
	paycode,
	feePercent,	// '手续费比例'
	warningAmount,	// '预警金额'
	currentBalance,	// '卡上余额'
	available,	// '是否可用'
	platformId,
	publicKey,
	merchantName,	// '商户名称'
	terminalType,
	pageType,
	domainName,	// '绑定的二级域名'
	key,	// '密钥'
	merchantNo;	// '商户号'

	public String getName(){
		return this.name();
	}
}

