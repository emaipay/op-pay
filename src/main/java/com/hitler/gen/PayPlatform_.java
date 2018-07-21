package com.hitler.gen;


public enum PayPlatform_ {

	createdBy,
	createdDate,
	lastModifiedBy,
	lastModifiedDate,
	id,
	name,	// '支付平台名称'
	platformTypeId,
	payUrl,	// '支付平台Url'
	platformLogo,
	platformCode;	// '支付类型code'

	public String getName(){
		return this.name();
	}
}

