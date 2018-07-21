package com.hitler.gen;


public enum PayTenantMerchant_ {

	createdBy,
	createdDate,
	lastModifiedBy,
	lastModifiedDate,
	id,
	payMerchantId,	// '商户表id'
	tenantId;	// '接入平台id'

	public String getName(){
		return this.name();
	}
}

