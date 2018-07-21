package com.hitler.core.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import freemarker.ext.beans.BeansWrapper;

/**
 * 全局公共配置信息
 * @author onsoul
 */

public class CommonDataInterceptor extends HandlerInterceptorAdapter {
	
	private Map<String,String> config=new HashMap<String,String>();

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String header = request.getHeader(HttpHeaders.ACCEPT);
		if (header != null && !header.contains("application/json")) {
			request.setAttribute("ctx", request.getContextPath());
			request.setAttribute("preference", config);
		}
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 上下文路径
		if (request.getAttribute("ctx") == null) {
			request.setAttribute("ctx", request.getContextPath());
		}
		request.setAttribute("enums", BeansWrapper.getDefaultInstance().getEnumModels());
		return super.preHandle(request, response, handler);
	}
	
	public void setConfig(Map<String, String> config) {
		this.config = config;
	}

}
