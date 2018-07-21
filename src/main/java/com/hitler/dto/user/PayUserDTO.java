package com.hitler.dto.user;

import com.hitler.core.dto.TransientDTO;
import com.hitler.entity.PayUser.AccountStatus;
import com.hitler.entity.PayUser.AccountType;
import com.hitler.entity.PayUser.OnlineStatus;

/**
 * 用户管理 Dto Thu May 04 11:27:04 CST 2017 klp
 */
public class PayUserDTO extends TransientDTO<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3577369570595735663L;

	private Integer id;

	/**
	 * 用户帐号
	 */
	private String userName;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 创建者
	 */
	private String createdBy;

	/**
	 * 创建时间
	 */
	private String createdDate;

	/**
	 * 最后修改人
	 */
	private String lastModifiedBy;

	/**
	 * 最后修改时间
	 */
	private String lastModifiedDate;

	/**
	 * 账户状态
	 */
	private AccountStatus accountLocked;

	/**
	 * 帐号类型
	 */
	private AccountType accountType;

	/**
	 * 登录冻结状态
	 */
	private boolean loginLocked;
	
	/**
	 * 登录密码
	 */
	private String pwdLogin;

	/**
	 * 在线状态
	 */
	private OnlineStatus onlineStatus;

	/**
	 * 上次登录IP地址
	 */
	private String lastLoginAddr;

	/**
	 * 上次登录时间
	 */
	private String lastLoginTime;

	/**
	 * 登录时间
	 */
	private String loginTime;

	/**
	 * 登录IP地址
	 */
	private String loginAddr;

	private String pwdSalt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}


	public String getPwdLogin() {
		return pwdLogin;
	}

	public void setPwdLogin(String pwdLogin) {
		this.pwdLogin = pwdLogin;
	}

	

	public AccountStatus getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(AccountStatus accountLocked) {
		this.accountLocked = accountLocked;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public boolean isLoginLocked() {
		return loginLocked;
	}

	public void setLoginLocked(boolean loginLocked) {
		this.loginLocked = loginLocked;
	}

	public OnlineStatus getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(OnlineStatus onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getLastLoginAddr() {
		return lastLoginAddr;
	}

	public void setLastLoginAddr(String lastLoginAddr) {
		this.lastLoginAddr = lastLoginAddr;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginAddr() {
		return loginAddr;
	}

	public void setLoginAddr(String loginAddr) {
		this.loginAddr = loginAddr;
	}

	public String getPwdSalt() {
		return pwdSalt;
	}

	public void setPwdSalt(String pwdSalt) {
		this.pwdSalt = pwdSalt;
	}
	
	

}
