package com.hitler.realm;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

public class ShiroSessionListener implements SessionListener {

	@Override
	public void onStart(Session session) {
	}

	@Override
	public void onStop(Session session) {
		clearAuth(session);
	}

	@Override
	public void onExpiration(Session session) {
		clearAuth(session);
	}

	/**
	 * 清除权限缓存，更改账号状态
	 */
	public void clearAuth(Session currentSession) {
		Object uid = currentSession.getAttribute("USER_ID");

		if (null != uid && !"".equals(uid.toString())) {
			//Long id = (Long) uid;
			//String cSID = currentSession.getId().toString();
		/*	String oldSID = Constants.sessionMap.get(id);
			if (StringUtils.isEmpty(oldSID) || cSID.equals(oldSID)) {
				try {
					Constants.sessionMap.remove(id);
					//userService.online(userService.find(id), OnlineState.off);
					//userService.clearCurReceiveNum(id);// 清空当前分配游客数//redis缓存的数据先不处理
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("", e);
				}
				logger.info("### userid:{} 清除权限缓存, 更新状态为离线", id);
			}*/
		}
	}
}
