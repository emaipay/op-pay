package com.hitler.core.web;

import java.util.HashMap;
import java.util.Map;

import com.hitler.core.enums.LockType;

/**
 * 业务锁 给业务加锁机制,如撤单,充值等等 防止重复撤单返款跟充值
 * 
 * @since 2016-12-05
 */
public class BusinessLock {
	// 保存撤单的Id， 防止重复提交
	private static Map<String, String> businessMap = new HashMap<String, String>();

	private LockType lockType;
	private String lockId;

	public BusinessLock(LockType lockType, String lockId) {
		this.lockType = lockType;
		this.lockId = lockId;
	}

	public boolean isLock() {
		synchronized (businessMap) {
			return businessMap.containsKey(lockType.getType() + "_" + lockId);
		}
	}

	/**
	 * 判断是否已经锁定， 如果没有则加锁并返回false。
	 * 
	 * @return
	 */
	public boolean checkAndLock() {
		synchronized (businessMap) {
			if (businessMap.containsKey(lockType.getType() + "_" + lockId)) {
				return true;
			} else {
				businessMap.put(lockType.getType() + "_" + lockId, "");
				return false;
			}
		}
	}

	public void unLock() {
		synchronized (businessMap) {
			businessMap.remove(lockType.getType() + "_" + lockId);
		}
	}

}
