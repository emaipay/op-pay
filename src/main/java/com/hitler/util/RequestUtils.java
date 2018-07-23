package com.hitler.util;

import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.UrlPathHelper;

/**
 * HttpServletRequest帮助类
 * 
 */
public class RequestUtils {
	public static int getIntParameter(HttpServletRequest request,String paramName,int defaultValue){
		String value = request.getParameter(paramName);
		if(StringUtils.isEmpty(value)){
			return defaultValue;
		}else{
			try{
				return Integer.parseInt(value);
			}catch(Exception e){
				return defaultValue;
			}
		}
	}
	
	public static long getLongParameter(HttpServletRequest request,String paramName,long defaultValue){
		String value = request.getParameter(paramName);
		if(StringUtils.isEmpty(value)){
			return defaultValue;
		}else{
			try{
				return Long.parseLong(value);
			}catch(Exception e){
				return defaultValue;
			}
		}
	}
	
	public static double getDoubleParameter(HttpServletRequest request,String paramName,double defaultValue){
		String value = request.getParameter(paramName);
		if(StringUtils.isEmpty(value)){
			return defaultValue;
		}else{
			try{
				return Double.parseDouble(value);
			}catch(Exception e){
				return defaultValue;
			}
		}
	}
	
	/*public static String getStringParameter(HttpServletRequest request,String paramName,String defaultValue){
		String value = request.getParameter(paramName);
		if(StringUtils.isBlank(value)){
			return defaultValue;
		}else{
			if(request.getMethod().equalsIgnoreCase(Constants.POST)){
				return value.trim();
			}else{
				try {
					return new String(value.trim().getBytes(Constants.ISO88591),Constants.UTF8);
				} catch (Exception e) {
					return defaultValue;
				}
			}
			
		}
	}*/

	public static float getFloatParameter(HttpServletRequest request,
			String paramName, float defaultValue) {
		String value = request.getParameter(paramName);
		if(StringUtils.isEmpty(value)){
			return defaultValue;
		}else{
			try{
				return Float.parseFloat(value);
			}catch(Exception e){
				return defaultValue;
			}
		}
	}

	public static Map<String, String> getRequestMap(HttpServletRequest request,
			String prefix) {
		return getRequestMap(request, prefix, false);
	}

	public static Map<String, String> getRequestMapWithPrefix(
			HttpServletRequest request, String prefix) {
		return getRequestMap(request, prefix, true);
	}

	private static Map<String, String> getRequestMap(
			HttpServletRequest request, String prefix, boolean nameWithPrefix) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> names = request.getParameterNames();
		String name, key, value;
		while (names.hasMoreElements()) {
			name = names.nextElement();
			if (name.startsWith(prefix)) {
				key = nameWithPrefix ? name : name.substring(prefix.length());
				value = StringUtils.join(request.getParameterValues(name), ',');
				map.put(key, value);
			}
		}
		return map;
	}

	/**
	 * 获取访问者IP
	 * 
	 * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
	 * 
	 * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
	 * 如果还不存在则调用Request .getRemoteAddr()。
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("x-forwarded-for");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("Proxy-Client-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("WL-Proxy-Client-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}

	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.contains(",")) {
			return ip.split(",")[0];
		} else {
			return ip;
		}
	}

	/*function get_client_ip() {
		if(getenv('HTTP_CLIENT_IP') && strcasecmp(getenv('HTTP_CLIENT_IP'), 'unknown')) {
			$ip = getenv('HTTP_CLIENT_IP');
		} elseif(getenv('HTTP_X_FORWARDED_FOR') && strcasecmp(getenv('HTTP_X_FORWARDED_FOR'), 'unknown')) {
			$ip = getenv('HTTP_X_FORWARDED_FOR');
		} elseif(getenv('REMOTE_ADDR') && strcasecmp(getenv('REMOTE_ADDR'), 'unknown')) {
			$ip = getenv('REMOTE_ADDR');
		} elseif(isset($_SERVER['REMOTE_ADDR']) && $_SERVER['REMOTE_ADDR'] && strcasecmp($_SERVER['REMOTE_ADDR'], 'unknown')) {
			$ip = $_SERVER['REMOTE_ADDR'];
		}
		return preg_match ( '/[\d\.]{7,15}/', $ip, $matches ) ? $matches [0] : '';
	}*/


	/**
	 * 获得当的访问路径
	 * 
	 * HttpServletRequest.getRequestURL+"?"+HttpServletRequest.getQueryString
	 * 
	 * @param request
	 * @return
	 */
	public static String getLocation(HttpServletRequest request) {
		UrlPathHelper helper = new UrlPathHelper();
		StringBuffer buff = request.getRequestURL();
		String uri = request.getRequestURI();
		String origUri = helper.getOriginatingRequestUri(request);
		buff.replace(buff.length() - uri.length(), buff.length(), origUri);
		String queryString = helper.getOriginatingQueryString(request);
		if (queryString != null) {
			buff.append("?").append(queryString);
		}
		return buff.toString();
	}

	/**
	 * 获得请求的session id，但是HttpServletRequest#getRequestedSessionId()方法有一些问题。
	 * 当存在部署路径的时候，会获取到根路径下的jsessionid。
	 * 
	 * @see HttpServletRequest#getRequestedSessionId()
	 * 
	 * @param request
	 * @return
	 *//*
	public static String getRequestedSessionId(HttpServletRequest request) {
		String sid = request.getRequestedSessionId();
		String ctx = request.getContextPath();
		// 如果session id是从url中获取，或者部署路径为空，那么是在正确的。
		if (request.isRequestedSessionIdFromURL() || StringUtils.isBlank(ctx)) {
			return sid;
		} else {
			// 手动从cookie获取
			Cookie cookie = CookieUtils.getCookie(request, Constants.JSESSION_COOKIE);
			if (cookie != null) {
				return cookie.getValue();
			} else {
				return null;
			}
		}

	}*/

	public static void main(String[] args) {
	}

	public static String getBodyAsString(HttpServletRequest request,
			String charset) {
		InputStream i = null;
		String str = null;
		try {
			i =request.getInputStream();
			int a = 0;
			int length = request.getContentLength();
			length = length <= 0 ? 1024 : length;
			byte[] body = new byte[0];
			byte[] tmp = new byte[1024];
			while ((a = i.read(tmp)) != -1) {
				int tmplength = body.length;
				body = new byte[tmplength + a];
				System.arraycopy(tmp, 0, body, tmplength, a);
			}
			str = URLDecoder.decode(new String(body,charset),charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
