package com.hitler.core.repository;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.Lists;
@SuppressWarnings("all")
public class DynamicSpecifications {
	
	public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters) {
		return new SpecificationImpl<T>(filters);
	}
	
	static class SpecificationImpl<T> implements Specification<T>, Serializable {
		
		private static final long serialVersionUID = 2404310650338699355L;
		
		private Collection<SearchFilter> filters;
		
		public SpecificationImpl(Collection<SearchFilter> filters) {
			this.filters = filters;
		}

		@Override
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
			if (filters != null && filters.size() > 0) {
				List<Predicate> predicates = Lists.newArrayList();
				for (SearchFilter filter : filters) {
					
					String[] names = StringUtils.split(filter.getFieldName(), ".");
					Path expression = root.get(names[0]);
					for (int i = 1; i < names.length; i++) {
						expression = expression.get(names[i]);
					}
					
					Object value = filter.getValue();
					if(expression.getJavaType().isAssignableFrom(Integer.class)) {
						// 整型
						value = Integer.parseInt(value.toString());
					}else if (expression.getJavaType().isEnum()) {
						// 枚举
						value = Enum.valueOf((Class)expression.getJavaType(), (String) value);
					} else if (expression.getJavaType().isAssignableFrom(Boolean.class)) {
						// 布尔
						value = Boolean.valueOf(value.toString());
					} else if (expression.getJavaType().isAssignableFrom(Date.class)) {
						// 时间
						try {
							value = DateUtils.parseDate(value.toString(), new String[]{"dd/MM/yyyy", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"});
						} catch (ParseException e) {
							throw new RuntimeException(e);
						}
					} else if (expression.getJavaType().isAssignableFrom(DateTime.class)) {
						// 时间戳
						try {
							long ms = DateUtils.parseDate(value.toString(), new String[]{"dd/MM/yyyy", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"}).getTime();
							DateTime t = new DateTime(ms);
							if (filter.getOp() == OP.LTE) {
								t = t.plus(999); // 增加毫秒数
							}
							value = t;
						} catch (ParseException e) {
							throw new RuntimeException(e);
						}
					}
					
					switch (filter.getOp()) {
					case EQ:
						predicates.add(builder.equal(expression, value));
						break;
					case NE:
						predicates.add(builder.notEqual(expression, value));
						break;
					case LIKE:
						predicates.add(builder.like(expression, "%" + value + "%"));
						break;
					case PLIKE:
						predicates.add(builder.like(expression, "%" + value));
						break;
					case ALIKE:
						predicates.add(builder.like(expression, value + "%"));
						break;
					case GT:
						predicates.add(builder.greaterThan(expression, (Comparable) value));
						break;
					case LT:
						predicates.add(builder.lessThan(expression, (Comparable) value));
						break;
					case GTE:
						predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) value));
						break;
					case LTE:
						predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) value));
						break;
					case IN:
						In in = (In) builder.in(expression);
						if (value != null && ((Collection) value).size() > 0) {
							Iterator iterator = ((Collection) value).iterator();
							while (iterator.hasNext()) {
								in.value(iterator.next());
							}
							predicates.add(in);
						} else {
							predicates.add(builder.equal(expression, -1));
						}
						break;
					}
				}
				
				if (!predicates.isEmpty()) {
					return builder.and(predicates.toArray(new Predicate[predicates.size()]));
				}
			}
			return builder.conjunction();
		}
		
	}
}
