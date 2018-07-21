package com.hitler.core.realm;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

public class SaltUtil {
	/**
	 * Protect from being instance.
	 */
	private SaltUtil() {
	}
	
	public static String getSalt(String s) {
		return getSalt(s, "");
	}

	public static String getSalt(String username, String password) {
		StringBuffer sb = null;
		sb = new StringBuffer();
		sb.append(username).append(password);
		return new Md5Hash(sb.toString(), RandomStringUtils.randomNumeric(6)).toHex();
	}
	
	public static String encodeMd5Hash(String pwd, String salt) {
		return new SimpleHash("md5", pwd, salt, 2).toHex();
	}

	public static void main(String[] args) {
		String salt = SaltUtil.getSalt("admin", "admin007");
		System.out.println(salt);
		String pwd = SaltUtil.encodeMd5Hash("admin007", salt);
		System.out.println(pwd);
	}
	
	
	public static final String PREFIX = "LANELIFE.CASINO";
	
	public static String encode(String str) {
		if (str == null) {
			return null;
		}
		return DigestUtils.md5Hex(PREFIX + str);
	}
	
	/*public static void main(String[] args) {
		String salt = getSalt("admin", "aaa000");
		String encodedPassword = encodeMd5Hash("000000", salt);
		System.out.println(salt+"|"+encodedPassword);
	}*/
}
