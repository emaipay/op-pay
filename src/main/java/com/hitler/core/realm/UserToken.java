package com.hitler.core.realm;


import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 装载用户登录信息，传递给shiro域进行认证使用
 * @author yang
 */
public class UserToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 5392652497148447543L;

	private String browserVersion; // 浏览器版本
	private String loginIp;        // 登录IP
	private String sessionId;      // 当前SESSIONID
	private String loginPassword;  // 登录密码


	public UserToken() {
		super();
	}

	public UserToken(String userName, String password,
					 boolean rememberMe, String browserVersion, String loginIp,
					 String sessionId) {
		super(userName, password, rememberMe);
		this.loginPassword = password;
		this.browserVersion = browserVersion;
		this.loginIp = loginIp;
		this.sessionId = sessionId;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	

	@Override
	public void clear() {
		super.clear();
		this.browserVersion = null;
		this.loginIp = null;
		this.loginPassword = null;
	}

}
