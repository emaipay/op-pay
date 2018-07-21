package com.hitler.core.web.view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.hitler.core.exception.BusinessException;
import com.hitler.core.web.resolver.MessageResolver;

public class MappingJacksonView extends MappingJackson2JsonView {

	@Override
	protected Object filterModel(Map<String, Object> model) {
		Map<String, Object> result = new HashMap<String, Object>(model.size());
		Set<String> renderedAttributes = (!CollectionUtils.isEmpty(this.getModelKeys()) ? this.getModelKeys() : model.keySet());
		for (Map.Entry<String, Object> entry : model.entrySet()) {
			if (renderedAttributes.contains(entry.getKey())) {
				if (entry.getValue() instanceof Exception) {
					result.clear(); //如果有错误则只返回错误信息
					Set<Map<String, String>> errorList = new HashSet<Map<String,String>>();
					
					Map<String, String> attr = new HashMap<String, String>();
					if (entry.getValue() instanceof BusinessException) {
						attr.put("message", MessageResolver.getMessage(((BusinessException)entry.getValue()).getCode()));
					} else {
						attr.put("message", MessageResolver.getMessage("com.hitler.exception.ServerInternalException"));
					}
					errorList.add(attr);
					
					result.put("errors", errorList);
					
				} else if (entry.getValue() instanceof BindingResult) {
					BindingResult br = (BindingResult) entry.getValue();
					if (br.hasFieldErrors()) {
						result.clear(); //如果有错误则只返回错误信息
						Set<Map<String, String>> errorList = new HashSet<Map<String,String>>();
						List<FieldError> errors = br.getFieldErrors();
						for (FieldError error : errors) {
							Map<String, String> attr = new HashMap<String, String>();
							attr.put("field", error.getField());
							attr.put("message", error.getDefaultMessage() != null ? error.getDefaultMessage() : MessageResolver.getMessage(error.getCode()));
							errorList.add(attr);
						}
						result.put("errors", errorList);
					}
				} else {
					result.put(entry.getKey(), entry.getValue());
				}
				
			}
		}
		return result;
	}
	
	

}
