package com.hitler.gen;


public enum PayTenant_ {

	createdBy,
	createdDate,
	lastModifiedBy,
	lastModifiedDate,
	id,
	merKey,
	memberDomain,
	available,	// '是否可用'
	terminalId,
	platformName,
	userName,
	userId,
	memberId;

	public String getName(){
		return this.name();
	}
}

