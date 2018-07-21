package com.hitler.core.web.resolver;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

public class MessageResolver {
	
	public static String getMessage(String code) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		WebApplicationContext context = RequestContextUtils.findWebApplicationContext(request);
		Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);
		return context.getMessage(code, null, locale);
	}

}
