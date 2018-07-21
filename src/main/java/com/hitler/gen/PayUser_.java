package com.hitler.gen;


public enum PayUser_ {

	createdBy,
	createdDate,
	lastModifiedBy,
	lastModifiedDate,
	id,
	accountLocked,
	pwdSalt,
	lastLoginTime,
	loginLocked,
	loginTime,
	nickName,
	accountType,	// '帐号类型'
	loginAddr,
	onlineStatus,
	pwdLogin,
	userName,
	lastLoginAddr;

	public String getName(){
		return this.name();
	}
}

