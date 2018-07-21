package com.hitler.core.realm;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.hitler.entity.PayUser;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 * @author yang
 * 
 */
public class ShiroUser implements Serializable {
	private static final long serialVersionUID = 1L;
	public Integer userId;
	public String userName;
	public String password;
	public String salt;
	public String remark;
	public PayUser.AccountType accountType;
	public String  tenantCode;

	public ShiroUser(Integer id, String userName, String password, String salt,PayUser.AccountType accountType,
					 String remark) {
		this.userId = id;
		this.userName = userName;
		this.password = password;
		this.accountType=accountType;
		this.salt = salt;
		this.remark = remark;
	}

	/**
	 * 本函数输出将作为默认的<shiro:principal/>输出.
	 */
	@Override
	public String toString() {
		return userName;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "userName");
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, "userName");
	}

	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public PayUser.AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(PayUser.AccountType accountType) {
		this.accountType = accountType;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}
}
