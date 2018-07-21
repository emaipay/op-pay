package com.hitler.realm;

import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.hitler.core.realm.SaltUtil;
import com.hitler.core.realm.ShiroUser;
import com.hitler.core.realm.UserToken;
import com.hitler.entity.PayUser;
import com.hitler.entity.PayUser.AccountStatus;
import com.hitler.service.IPayPermissionService;
import com.hitler.service.IPayRolePermissionService;
import com.hitler.service.IPayRoleService;
import com.hitler.service.IPayUserService;

/**
 * 权限域,用于处理用户认证与授权 认证:判定用户账号密码 授权:读取用户的角色与权限
 * 认证与授权产生的对应信息会交给Shiro管理,这两份信息实质会交给WEB Session. 以供前端视图与控制器做相应的判定处理
 * 
 * @author JTWise 2016年7月25日 上午10:18:22
 */
public class UserRealm extends AuthorizingRealm {

	@Resource
	private IPayUserService payUserService;
	@Resource
	private IPayRolePermissionService payRolePermissionService;
	@Resource
	private IPayRoleService payRoleService;
	@Resource
	private IPayPermissionService payPermissionService;

	// 授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		SimpleAuthorizationInfo auth = new SimpleAuthorizationInfo();
		Object obj = principals.getPrimaryPrincipal();

		if (obj instanceof ShiroUser) {
			ShiroUser shiroUser = (ShiroUser) obj;
			Integer userId = shiroUser.getUserId();
			Set<String> roleSet = payRoleService.getRoleSet(userId);
			// 用户角色授权
			if (roleSet != null) {
				auth.setRoles(roleSet);
			}
			Set<String> permissionSet = payPermissionService.getPermissionSet(userId);
			if (permissionSet != null) {
				auth.addStringPermissions(permissionSet);
			}
		}

		return auth;
	}

	/**
	 * 认证回调函数， 登陆时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		UserToken token = (UserToken) authcToken;
		String t_userName = token.getUsername();
		PayUser u = payUserService.findByUserName(t_userName);
		// 用户是否存在
		if (null == u) {
			throw new UnknownAccountException("帐号不存在！");
		}
		// 账号状态
		if (u.getAccountLocked().equals(AccountStatus.停用)) {
			
			throw new LockedAccountException("账号已停用！");
		}
		if (u.getAccountLocked().equals(AccountStatus.锁定)) {
			
			throw new LockedAccountException("账号被锁定！");
		}
		if(u.getLoginLocked()){
			throw new LockedAccountException("账号登录锁定！");
		}

		
		// 密码检验
		boolean effectivePwd = false;
		if (effectivePwd == false) {
			// 密码是否正确
			String paswordMd5 = SaltUtil.encodeMd5Hash(token.getLoginPassword(), u.getPwdSalt());
			effectivePwd = u.getPwdLogin().equals(paswordMd5);

		}
		if (effectivePwd == false) {
			// 进入尝试登陆业务流程：
			// 若密码错误次数<=N次：累计登陆失败次数（可设置）、写登陆失败日志（区分管理员和用户）
			// 若密码错误次数>N次：更新锁定用户登陆锁定状态、添加至自动解锁登陆（登陆时长可设置）、用户操作日志
			throw new IncorrectCredentialsException("密码错误！");
		}
		String pwd = u.getPwdLogin();
		// remark备注字段已分表
		ShiroUser SU = new ShiroUser(u.getId(), u.getUserName(), pwd, u.getPwdSalt(), u.getAccountType(), "");
		ByteSource salt_pwd = ByteSource.Util.bytes(u.getPwdSalt());
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(SU, pwd, salt_pwd, getName());
		// 登陆成功：更新用户状态,报表登陆状态,写日志,权限处理
		onSuccess(u,token);
		return authenticationInfo;
	}

	/**
	 * 权限与redis做缓存处理
	 */
	private void onSuccess(PayUser user,UserToken token){
		try {
			payUserService.onLoginSuccess(user, user.getLoginAddr());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	protected void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	protected void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}
	
	public static void main(String[] args) {
		String paswordMd5 = SaltUtil.encodeMd5Hash("mjjcai009!", "3c800dafc9614581abba6a2f25dd3aeb");
		System.out.println(paswordMd5);
	}

}
