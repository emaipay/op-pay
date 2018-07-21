package com.hitler.gen;


public enum PayPlatformType_ {

	createdBy,
	createdDate,
	lastModifiedBy,
	lastModifiedDate,
	id,
	platformType,	// '支付平台类型'
	typeName;	// '支付平台名称'

	public String getName(){
		return this.name();
	}
}

