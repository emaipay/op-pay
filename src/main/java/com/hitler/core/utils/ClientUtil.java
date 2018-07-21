package com.hitler.core.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.hitler.core.Constants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet工具类
 * @author yang
 *
 */
public class ClientUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ClientUtil.class);

	/**
	 * 获取session中的验证码
	 * @param request
	 * @return
     */
	public static String getVCode(HttpServletRequest request) {
		return (String) request.getSession().getAttribute(Constants.VCODE);
	}

	/**
	 * 登陆成功后移除session中的验证码
	 * @param request
	 * @return
	 */
	public static void removeVCode(HttpServletRequest request) {
		request.getSession(false).removeAttribute(Constants.VCODE);
	}

	/**
	 * 获取sessionID
	 * @param request
	 * @return
	 */
	public static String getSessionId(HttpServletRequest request) {
		return (String)request.getSession().getId();
	}


	/**
	 * 返回客户端浏览器的版本号、类型
	 */
	public static String getBrowser(HttpServletRequest request) {
		String browserInfo = "other";
		String ua = request.getHeader("User-Agent").toLowerCase();
		String s;
		String version;
		String msieP = "msie ([\\d.]+)";
		String ieheighP = "rv:([\\d.]+)";
		String firefoxP = "firefox\\/([\\d.]+)";
		String chromeP = "chrome\\/([\\d.]+)";
		String operaP = "opr.([\\d.]+)";
		String safariP = "version\\/([\\d.]+).*safari";

		Pattern pattern = Pattern.compile(msieP);
		Matcher mat = pattern.matcher(ua);
		if (mat.find()) {
			s = mat.group();
			version = s.split(" ")[1];
			browserInfo = "ie " + version.substring(0, version.indexOf("."));
			return browserInfo;
		}

		pattern = Pattern.compile(firefoxP);
		mat = pattern.matcher(ua);
		if (mat.find()) {
			s = mat.group();
			version = s.split("/")[1];
			browserInfo = "firefox "
					+ version.substring(0, version.indexOf("."));
			return browserInfo;
		}

		pattern = Pattern.compile(ieheighP);
		mat = pattern.matcher(ua);
		if (mat.find()) {
			s = mat.group();
			version = s.split(":")[1];
			browserInfo = "ie " + version.substring(0, version.indexOf("."));
			return browserInfo;
		}

		pattern = Pattern.compile(operaP);
		mat = pattern.matcher(ua);
		if (mat.find()) {
			s = mat.group();
			version = s.split("/")[1];
			browserInfo = "opera " + version.substring(0, version.indexOf("."));
			return browserInfo;
		}

		pattern = Pattern.compile(chromeP);
		mat = pattern.matcher(ua);
		if (mat.find()) {
			s = mat.group();
			version = s.split("/")[1];
			browserInfo = "chrome "
					+ version.substring(0, version.indexOf("."));
			return browserInfo;
		}

		pattern = Pattern.compile(safariP);
		mat = pattern.matcher(ua);
		if (mat.find()) {
			s = mat.group();
			version = s.split("/")[1].split(" ")[0];
			browserInfo = "safari "
					+ version.substring(0, version.indexOf("."));
			return browserInfo;
		}
		return browserInfo;
	}

	/**
	 * 获得真实IP地址。在使用了反向代理时，
	 * 直接用HttpServletRequest.getRemoteAddr()无法获取客户真实的IP地址。
	 * @param request
	 * @return
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		
		//2016-05-01 ml 处理多IP地址，截取第一个用户IP
		if(ip!=null){
			String[] ips = ip.split(",", -1);
			if(ips.length>1){
				ip = ips[0];
			}
		}
		
		return ip;
	}



	/**
	 * =========================================以下是未用到的方法(预留在此)==============================================
	 */
    public static void remove(String key) {
		Subject subject = SecurityUtils.getSubject();
		subject.getSession().removeAttribute(key);
	}

	public static String getCookie(HttpServletRequest request, String name) {
		Assert.notNull(request, "Request must not be null");
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	/**
	 * 设置禁止客户端缓存的Header.
	 */
	public static void setNoCacheHeader(HttpServletResponse response) {
		// Http 1.0 header
		response.setDateHeader("Expires", 1L);
		response.addHeader("Pragma", "no-cache");
		// Http 1.1 header
		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
	}

	/**
	 * 输出html。并禁止客户端缓存。输出json也可以用这个方法。
	 *
	 * contentType:text/html;charset=utf-8。
	 *
	 * @param response
	 * @param s
	 */
	public static void writeHtml(HttpServletResponse response, String s) {
		response.setContentType("text/html;charset=utf-8");
		setNoCacheHeader(response);
		try {
			response.getWriter().write(s);
		} catch (IOException ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
    
}
