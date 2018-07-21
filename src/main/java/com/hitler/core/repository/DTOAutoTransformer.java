package com.hitler.core.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.Column;

import org.hibernate.HibernateException;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.StringUtils;

import com.hitler.core.enums.PersistEnum;

/**
 * Created by lichengqi on 2017/4/21.
 * Desc
 */
@SuppressWarnings("all")
public class DTOAutoTransformer<T> implements ResultTransformer {
	private static final long serialVersionUID = 1L;
	
	private final Class<T> resultClass;
	
	public DTOAutoTransformer(Class<T> resultClass) {
		if (resultClass == null)
			throw new IllegalArgumentException("resultClass cannot be null");
		this.resultClass = resultClass;
	}
	
	
	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Object result = null;
		try {
			result = resultClass.newInstance();
			for (int i = 0; i < aliases.length; i++) {
				if (tuple[i] != null) {
					Class<?> tupleClass = tuple[i].getClass();
					
					String alias = aliases[i];
					Field[] fields = resultClass.getDeclaredFields();
					boolean looked = false;
					if (fields != null && fields.length > 0) {
						for (Field field : fields) {
							Column annotation = field.getAnnotation(Column.class);
							if (annotation == null) continue;
							
							String name = annotation.name();
							if (null != name && !"".equals(name) && alias.toLowerCase().equals(name.toLowerCase())) {
								looked = true;
								if (field != null) {
									field.setAccessible(true);
									trySetValue(field, result, tuple[i]);
									break;
								}
							}
						}
					}
					//如果没有找到以注解匹配的字段，则常识把下划线改成驼峰来找到方法
					if (!looked) {
						alias = QueryUtils.underlineToCamel(alias);
						Field field = getDeclaredField(result, alias);
						if (field != null) {
							field.setAccessible(true);
							if (field.get(result) == null)
								trySetValue(field, result, tuple[i]);
						}
					}
				}
			}
		} catch (InstantiationException e) {
			throw new HibernateException("Could not instantiate resultclass: "
					+ resultClass.getName());
		} catch (IllegalAccessException e) {
			throw new HibernateException("Could not instantiate resultclass: "
					+ resultClass.getName());
		}
		return result;
	}
	
	
	/**
	 * @param field
	 * @param result
	 * @param value
	 */
	public static void trySetValue(Field field, Object result, Object value) {
		Class<?> tupleClass = value.getClass();
		Class<?> fieldClass = field.getType();
		try {
			if (!fieldClass.equals(tupleClass)) {
				if (Number.class.isAssignableFrom((tupleClass))) {
					Number num = (Number) value;
					if (Short.class.equals(fieldClass)) {
						field.set(result, num.shortValue());
					} else if (Long.class.equals(fieldClass)) {
						field.set(result, num.longValue());
					} else if (Integer.class.equals(fieldClass)) {
						field.set(result, num.intValue());
					} else if (Double.class.equals(fieldClass)) {
						field.set(result, num.doubleValue());
					} else if (Boolean.class.equals(fieldClass)) {
						field.set(result, num.intValue() == 1);
					} else if (Float.class.equals(fieldClass)) {
						field.set(result, num.floatValue());
					} else if (BigDecimal.class.equals(fieldClass)) {
						field.set(result, new BigDecimal(value + ""));
						//枚举类型转换
					} else if (fieldClass.isEnum()) {
						field.set(result, getEnumByValue(fieldClass.getEnumConstants(), value));
					} else {
						field.set(result, value);
					}
					//如果tuple为参数的子类,直接设置
					//如java.util.Date; java.sql.Date;
				} else if (fieldClass.isAssignableFrom(tupleClass)) {
					field.set(result, value);
					//如果是枚举
				} else if (fieldClass.isEnum()) {
					field.set(result, getEnumByValue(fieldClass.getEnumConstants(), 1));
				} else {
					field.set(result, value);
				}
			} else {
				field.set(result, value);
			}
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过枚举值获取枚举
	 * @param constants
	 * @param value
	 * @return
	 */
	private static PersistEnum getEnumByValue(Object[] constants, Object value) {
		for(Object obj: constants) {
			PersistEnum em = (PersistEnum)obj;
			if (em.getValue().equals(value + "")) {
				return em;
			}
		}
		return null;
	}
	
	/**
	 * 循环向上转型
	 *
	 * @param object
	 * @param fieldName
	 * @return
	 */
	public static Field getDeclaredField(Object object, String fieldName) {
		Field field = null;
		Class<?> clazz = object.getClass();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
				return field;
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	
	/**
	 * 循环向上转型, 获取对象的 DeclaredMethod
	 *
	 * @param object         : 子类对象
	 * @param methodName     : 父类中的方法名
	 * @param parameterTypes : 父类中的方法参数类型
	 * @return 父类中的方法对象
	 */
	public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
		Method method = null;
		
		for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				method = clazz.getDeclaredMethod(methodName, parameterTypes);
				return method;
			} catch (Exception e) {
			}
		}
		
		return null;
	}
	
	/**
	 * 拼接在某属性的 set方法
	 *
	 * @param fieldName
	 * @return String
	 */
	private static String parSetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		return "set" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
	}
	
	/**
	 * 对于Oracle返回的列全部大写需要进行转换, 其他数据库可能不需要这么处理
	 *
	 * @param columnName
	 * @return
	 */
	public String convertColumnToProperty(String columnName) {
		columnName = columnName.toLowerCase();
		StringBuffer buff = new StringBuffer(columnName.length());
		StringTokenizer st = new StringTokenizer(columnName, "_");
		while (st.hasMoreTokens()) {
			buff.append(StringUtils.capitalize(st.nextToken()));
		}
		buff.setCharAt(0, Character.toLowerCase(buff.charAt(0)));
		return buff.toString();
	}
	
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List transformList(List collection) {
		return (List<T>) collection;
	}
	
}
