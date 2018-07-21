package com.hitler.core.enums;
/**
 * @since 2016-12-05
 */
public enum LockType {
	充值回调("rechargeCallback");
	
	private String type;

	private LockType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
}
