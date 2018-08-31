package com.hitler.gen;


public enum PayOrder_ {

	createdBy,
	createdDate,
	lastModifiedBy,
	lastModifiedDate,
	id,
	summary,	// '描述'
	openid,	// '微信openid'
	fee,	// '手续费'
	orderStatusDesc,	// '支付结果描述'
	orderStatus,	// '订单状态'
	platformId,
	userName,	// '回调地址'
	userId,	// '充值用户id'
	payerBankId,
	transactionId,	// '接入方返回的订单号'
	terminalType,
	connBillno,	// '接入平台的订单号'
	payerSecret,	// 'SECRET'
	orderAmount,	// '订单金额'
	factAmount,	// '实际金额'
	merchantId,
	tenantId,
	reqReserved,	// '请求保留信息'
	notifyUrl,	// '异步回调地址'
	transBillNo,	// '第三方支付单号'
	sourceUserId,	// '充值用户id'
	returnUrl,	// '页面回调地址'
	billNo;	// '订单号'

	public String getName(){
		return this.name();
	}
}

