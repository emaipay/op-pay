package com.hitler.core.web.resolver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SortArgumentResolver extends SortHandlerMethodArgumentResolver {

	@Override
	public Sort resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		String[] directionParameter = webRequest.getParameterValues(getSortParameter(parameter));
		// 0 = a,asc
		// 1 = b1,asc;b2,desc
		// 2 = c,desc
		if (directionParameter != null && directionParameter.length != 0) {
			StringBuilder sb = new StringBuilder();
			for (String part : directionParameter) {
				sb.append(part).append(";");
			}
			return parameterIntoSort(sb.toString().split(";"), ",");
		} else {
			return super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
		}
	}
	
	
	public Sort parameterIntoSort(String[] source, String delimiter) {

		List<Order> allOrders = new ArrayList<Sort.Order>();

		for (String part : source) {

			if (part == null) {
				continue;
			}

			String[] elements = part.split(delimiter);
			Direction direction = elements.length == 0 ? null : Direction.fromStringOrNull(elements[elements.length - 1]);

			for (int i = 0; i < elements.length; i++) {

				if (i == elements.length - 1 && direction != null) {
					continue;
				}

				String property = elements[i];

				if (!StringUtils.hasText(property)) {
					continue;
				}

				allOrders.add(new Order(direction, property));
			}
		}

		return allOrders.isEmpty() ? null : new Sort(allOrders);
	}
	
	

}
