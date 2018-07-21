package com.hitler.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import com.hitler.core.entity.CheckableEntity;
import com.hitler.core.enums.PersistEnum;

/**
 * 用户
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_pay_user")
public class PayUser extends CheckableEntity<Integer> {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -7791257400520912773L;
	/**
	 * 账户状态
	 */
	@Type(type = "com.hitler.core.enums.MyEnumType")
	@Column(name = "ACCOUNT_LOCKED",columnDefinition="tinyint")
	private AccountStatus accountLocked = AccountStatus.正常;
	/**
	 * 帐号类型
	 */
	@Type(type = "com.hitler.core.enums.MyEnumType")
	@Column(name = "ACCOUNT_TYPE", columnDefinition = "tinyint COMMENT '帐号类型'", nullable = false)
	private AccountType accountType;
	/**
	 * 最后登录IP地址
	 */
	@Column(name = "LAST_LOGIN_ADDR")
	private String lastLoginAddr;
	/**
	 * 上次登录时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_LOGIN_TIME")
	private Date lastLoginTime;
	/**
	 * 登录IP地址
	 */
	@Column(name = "LOGIN_ADDR")
	private String loginAddr = "";
	/**
	 * 登录冻结状态
	 */
	@Column(name = "LOGIN_LOCKED")
	private Boolean loginLocked = Boolean.FALSE;
	/**
	 * 登录时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOGIN_TIME")
	private Date loginTime;
	
	/**
	 * 昵称
	 */
	@Column(name = "NICK_NAME")
	private String nickName = "";
	/**
	 * 在线状态
	 */
	@Column(name = "ONLINE_STATUS",columnDefinition="tinyint")
	@Type(type = "com.hitler.core.enums.MyEnumType")
	private OnlineStatus onlineStatus = OnlineStatus.离线;
	/**
	 * 登录密码
	 */
	@Column(name = "PWD_LOGIN")	
	private String pwdLogin;
	
	/**
	 * 登录密码
	 */
	@Column(name = "PWD_SALT")	
	private String pwdSalt;
	/**
	 * 用户帐号
	 */
	@Column(name = "USER_NAME")
	private String userName;
	
	public PayUser() {
	}

	/**
	 * 账户状态
	 */
	public enum AccountStatus implements PersistEnum<AccountStatus> {
		正常("0"), 锁定("1"), 停用("2");
		private final String value;

		AccountStatus(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
		}
	}

	/**
	 * 帐号类型
	 */
	public enum AccountType implements PersistEnum<AccountType> {
		管理员("0"), 租户("1");
		private final String value;

		AccountType(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
		}
	}



	/**
	 * 在线状态
	 */
	public enum OnlineStatus implements PersistEnum<OnlineStatus> {
		在线("1"), 离线("2");
		private final String value;

		OnlineStatus(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
		}
	}



	public AccountStatus getAccountLocked() {
		return this.accountLocked;
	}

	public void setAccountLocked(AccountStatus accountLocked) {
		this.accountLocked = accountLocked;
	}

	public AccountType getAccountType() {
		return this.accountType;
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

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLoginAddr() {
		return loginAddr;
	}

	public void setLoginAddr(String loginAddr) {
		this.loginAddr = loginAddr;
	}

	public Boolean getLoginLocked() {
		return loginLocked;
	}

	public void setLoginLocked(Boolean loginLocked) {
		this.loginLocked = loginLocked;
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

	public OnlineStatus getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(OnlineStatus onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getPwdLogin() {
		return pwdLogin;
	}

	public void setPwdLogin(String pwdLogin) {
		this.pwdLogin = pwdLogin;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwdSalt() {
		return pwdSalt;
	}

	public void setPwdSalt(String pwdSalt) {
		this.pwdSalt = pwdSalt;
	}

	

}