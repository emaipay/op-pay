package com.hitler.dto.user;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.hitler.core.dto.TransientDTO;
import com.hitler.entity.PayUser.AccountType;

/**
 * 用户管理 Dto Thu May 04 11:27:04 CST 2017 klp
 */
public class PayUserCreateDTO extends TransientDTO<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8034593329837690253L;

	/**
	 * 管理员账号
	 */
	@NotNull
	private String userName;
	/**
	 * 管理员密码
	 */
	private String pwdLogin;
	/**
	 * 加盐
	 */
	private String salt;

	/**
	 * 最后登录时间
	 */
	private Date loginTime;
	/**
	 * 管理员昵称
	 */
	private String nickName;
	/**
	 * 注册时间
	 */
	private Date createdDate;
	/**
	 * 账号类型
	 */
	private AccountType accountType;
	/**
	 * 最后登录IP地址
	 */
	private String lastLoginAddr = "";
	/**
	 * 登录冻结状态
	 */
	private Boolean loginLocked = Boolean.FALSE;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwdLogin() {
		return pwdLogin;
	}

	public void setPwdLogin(String pwdLogin) {
		this.pwdLogin = pwdLogin;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public String getLastLoginAddr() {
		return lastLoginAddr;
	}

	public void setLastLoginAddr(String lastLoginAddr) {
		this.lastLoginAddr = lastLoginAddr;
	}

	public Boolean getLoginLocked() {
		return loginLocked;
	}

	public void setLoginLocked(Boolean loginLocked) {
		this.loginLocked = loginLocked;
	}

}
