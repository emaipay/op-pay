package com.hitler.gen;


public enum PayPlatformConfig_ {

	createdBy,
	createdDate,
	lastModifiedBy,
	lastModifiedDate,
	id,
	classPath,	// '类路径'
	platformId,
	platformName,	// '支付平台名称'
	platformCode;	// '支付类型code'

	public String getName(){
		return this.name();
	}
}

