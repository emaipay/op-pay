package com.hitler.core.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.metamodel.SingularAttribute;
import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebUtils;

import com.google.common.collect.Lists;

public class SearchFilter implements Serializable {

	private static final String FIELD_NAME_OR_FLAG = "&";
	private static final Logger logger = LoggerFactory.getLogger(SearchFilter.class);

	private static final long serialVersionUID = 8719997267153871707L;

	public static final String PREFIX = "search_";

	public enum JoinType {
		DEFAULT, INNER, LEFT, RIGHT
	}

	private String fieldName;
	private Object value;
	private OP op;
	private JoinType joinType = JoinType.DEFAULT;

	public SearchFilter(String fieldName, OP op, Object value) {
		this(fieldName, op, value, JoinType.DEFAULT);
	}

	public enum Operator {
		/**
		 * EQ : 等于 = 
		 * NE : 不等于 != 
		 * LIKE : 相似 = like("%" + value + "%") 
		 * PLIKE : like("%" + value) 
		 * ALIKE : like(value + "%") 
		 * GT : 大于 > 
		 * LT : 小于 < 
		 * GTE : 大于等于 >= 
		 * LTE : 小于等于 <= IN : id in (1,2,3)
		 */
		EQ, NE, LIKE, PLIKE, ALIKE, GT, LT, GTE, LTE, IN, OR
	}
	
	public SearchFilter(String fieldName, OP op, Object value, JoinType joinType) {
		logger.debug("###search parameter:" + fieldName + "<[" + op + "]>" + value);
		this.fieldName = fieldName;
		this.op = op;
		this.value = value;
		this.joinType = joinType;
	}

	public SearchFilter(SingularAttribute<?, ?> attribute, OP op, Object value) {
		this(attribute.getName(), op, value, JoinType.DEFAULT);
	}

	public String getFieldName() {
		return fieldName;
	}

	public Object getValue() {
		return value;
	}

	public OP getOp() {
		return op;
	}

	public JoinType getJoinType() {
		return joinType;
	}

	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}

	public static List<SearchFilter> parse(ServletRequest request) {
		return parse(WebUtils.getParametersStartingWith(request, PREFIX));
	}

	public static List<SearchFilter> parse(Map<String, Object> searchParams) {
		List<SearchFilter> filters = Lists.newArrayList();
		for (Entry<String, Object> entry : searchParams.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (StringUtils.isBlank((String) value)) {
				continue;
			}
			String[] names = StringUtils.split(key, "_");
			String fieldName = null;
			OP op = null;
			JoinType joinType = JoinType.DEFAULT;
			if (names.length == 2) {
				op = OP.valueOf(names[0]);
				fieldName = names[1];
			} else if (names.length == 3) {
				joinType = JoinType.valueOf(names[0]);
				op = OP.valueOf(names[1]);
				fieldName = names[2];
			} else {
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			}
			String[] fieldNameArray = fieldName.split(FIELD_NAME_OR_FLAG);
			SearchFilter filter = null;
			if (fieldNameArray.length == 1) {
				filter = new SearchFilter(fieldName, op, value, joinType);
			} else {
				List<SearchFilter> subFilterList = new ArrayList<>();
				for (String name : fieldNameArray) {
					subFilterList.add(new SearchFilter(name, op, value, joinType));
				}
				filter = new SearchFilter(fieldNameArray[0], OP.OR, subFilterList, joinType);
			}
			filters.add(filter);
		}
		return filters;
	}

	public static List<SearchFilter> find(Collection<SearchFilter> filters, String fieldName) {
		List<SearchFilter> existList = new ArrayList<>();
		for (SearchFilter searchFilter : filters) {
			if (StringUtils.equalsIgnoreCase(searchFilter.getFieldName(), fieldName)) {
				existList.add(searchFilter);
			}
		}
		return existList;
	}

	public static SearchFilter findFirst(Collection<SearchFilter> filters, String fieldName) {
		List<SearchFilter> list = find(filters, fieldName);
		return list.isEmpty() ? null : list.get(0);
	}

	public static void remove(Collection<SearchFilter> filters, String fieldName) {
		List<SearchFilter> removeList = find(filters, fieldName);
		for (SearchFilter searchFilter : removeList) {
			filters.remove(searchFilter);
		}
	}

	public static void replaceFieldName(Collection<SearchFilter> filters, String fieldName, String newFieldName) {
		for (SearchFilter searchFilter : filters) {
			if (StringUtils.equalsIgnoreCase(searchFilter.getFieldName(), fieldName)) {
				searchFilter.fieldName = newFieldName;
			}
		}
	}

}
