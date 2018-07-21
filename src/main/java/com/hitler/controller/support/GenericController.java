package com.hitler.controller.support;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.ReflectionUtils.AnnotationFieldFilter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.google.common.collect.Sets;
import com.hitler.core.dto.annotion.DisallowedField;
import com.hitler.core.dto.support.GenericDTO;

public abstract class GenericController {
	protected Logger log = LoggerFactory.getLogger(GenericController.class);

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		if (binder.getTarget() != null && binder.getTarget() instanceof GenericDTO) {
			AnnotationDisallowedFieldMetadata metadata = AnnotationDisallowedFieldMetadata
					.getMetadata(binder.getTarget().getClass());
			String[] fs = new String[metadata.getDisallowedFields().size()];
			metadata.getDisallowedFields().toArray(fs);
			binder.setDisallowedFields(fs);
		}
	}

}

class AnnotationDisallowedFieldMetadata {

	private static final Map<Class<?>, AnnotationDisallowedFieldMetadata> METADATA_CACHE = new ConcurrentHashMap<Class<?>, AnnotationDisallowedFieldMetadata>();
	private static final AnnotationFieldFilter DISALLOWED_FIELD_FILTER = new AnnotationFieldFilter(
			DisallowedField.class);

	private Set<String> disallowedFields = Sets.newHashSet();

	public AnnotationDisallowedFieldMetadata(Class<?> type) {
		while (type != Object.class) {
			for (Field field : type.getDeclaredFields()) {
				if (DISALLOWED_FIELD_FILTER.matches(field)) {
					disallowedFields.add(field.getName());
				}
			}
			type = type.getSuperclass();
		}
	}

	public static AnnotationDisallowedFieldMetadata getMetadata(Class<?> type) {

		if (METADATA_CACHE.containsKey(type)) {
			return METADATA_CACHE.get(type);
		}

		AnnotationDisallowedFieldMetadata metadata = new AnnotationDisallowedFieldMetadata(type);
		METADATA_CACHE.put(type, metadata);
		return metadata;
	}

	public Set<String> getDisallowedFields() {
		return disallowedFields;
	}

}