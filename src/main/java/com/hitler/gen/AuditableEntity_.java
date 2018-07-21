package com.hitler.gen;


public enum AuditableEntity_ {

	id,
	createdDate,	// '创建时间'
	createdBy,
	lastModifiedDate,	// '操作时间'
	lastModifiedBy;

	public String getName(){
		return this.name();
	}
}

