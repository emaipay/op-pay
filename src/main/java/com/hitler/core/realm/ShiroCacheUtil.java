package com.hitler.core.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Servlet工具类
 * @author yang
 *
 */
public class ShiroCacheUtil {
	
	/**
	 * 获取shiro缓存中用户的租户代号
	 */
	public static String getUserTenantCode() {
		ShiroUser shiroUser = getCurShiroUser();
		if(shiroUser != null)
			return shiroUser.tenantCode;
		return null;

	}
	/**
	 * 获取shiro缓存中的UserId
	 */
	public static Integer getUserId() {
		ShiroUser shiroUser = getCurShiroUser();
		if(shiroUser != null)
			return shiroUser.getUserId();
		return null;

	}
	/**
	 * 获取shiro缓存中的UserName
	 */
	public static String getUserName() {
		ShiroUser shiroUser = getCurShiroUser();
		if(shiroUser != null)
			return shiroUser.getUserName();
		return null;

	}

	/**
	 * 获取shiro缓存中的ShiroUser对象,包括
	 id; //用户id
	 userName;//用户名
	 password;//用户密码
	 salt;//盐
	 remark;//备注
	 accountType;//账号类型
	 tenantCode;//租户代号
	 */
	public static ShiroUser getCurShiroUser() {
		Subject subject = SecurityUtils.getSubject();
		if(subject==null)
			return null;
 		return (ShiroUser)subject.getPrincipal();
	}
}
