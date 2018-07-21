package com.hitler.gen;


public enum PayBank_ {

	createdBy,
	createdDate,
	lastModifiedBy,
	lastModifiedDate,
	id,
	bankTransferStatus,	// '线下充值状态'
	name,	// '银行名称'
	logoFilePath,	// '银行Logo图片路径'
	allowBindingStatus,	// '是否允许绑定'
	shortName;	// '银行简称'

	public String getName(){
		return this.name();
	}
}

