package com.hitler.gen;


public enum PayTenantLimit_ {

	createdBy,
	createdDate,
	lastModifiedBy,
	lastModifiedDate,
	id,
	onetimeRechargeAmountMin,	// '单笔最小充值金额'
	dailyRechargeTimesMax,	// '每日最大充值次数'
	onetimeRechargeAmountMax,	// '单笔最大充值金额'
	dailyRechargeAmountMax,	// '每日最大充值金额'
	tenantId,	// '是哪个接入平台'
	platformId,	// '第三方支付商户ID'
	platformCode;	// '第三方支付商户CODE'

	public String getName(){
		return this.name();
	}
}

