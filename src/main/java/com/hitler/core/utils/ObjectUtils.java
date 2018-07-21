package com.hitler.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Object类型转换工具
 * 
 * @author
 */
public class ObjectUtils {
	private static final Logger logger = LoggerFactory.getLogger(ObjectUtils.class);
	
	/**
	 * 转换为Date类型
	 * 
	 * @param obj
	 *            输入对象
	 * @return Date
	 */
	public static Date toDate(Object obj) {
		if (obj == null) {
			return null;// null默认返回值
		}

		if (obj instanceof Date) {
			return (Date) obj;
		}

		// 尝试 YYYYMMDDHH24MISS格式转换
		if (obj instanceof String) {
			return DateUtil.str2Date((String) obj);
		}

		// 不支持的类型转换
		return null;
	}

	/**
	 * 转为整型
	 * 
	 * @param obj
	 *            输入对象
	 * @return int整型，如果obj不是整型默认返回0
	 */
	public static int toInt(Object obj) {
		return toInt(obj, 0);
	}

	/**
	 * 转为整型
	 * 
	 * @param obj
	 *            输入对象
	 * @param defaultValue
	 *            默认值
	 * @return int整型，如果obj不是整型默认返回默认值
	 */
	public static int toInt(Object obj, int defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		if (obj instanceof Number) {
			return ((Number) obj).intValue();
		}
		return NumberUtils.toInt(obj.toString(), defaultValue);
	}

	/**
	 * 转为long
	 * 
	 * @param obj
	 *            输入对象
	 * @return long整型，如果obj不是整型默认返回0
	 */
	public static long toLong(Object obj) {
		return toLong(obj, 0);
	}

	/**
	 * 转为long
	 * 
	 * @param obj
	 *            输入对象
	 * @param defaultValue
	 *            默认值
	 * @return long整型，如果obj不是整型默认返回默认值
	 */
	public static long toLong(Object obj, long defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		if (obj instanceof Number) {
			return ((Number) obj).longValue();
		}
		return NumberUtils.toLong(obj.toString(), defaultValue);
	}

	/**
	 * 转为float
	 * 
	 * @param obj
	 *            输入对象
	 * @return double，如果obj不是数值默认返回0
	 */
	public static float toFloat(Object obj) {
		return toFloat(obj, 0);
	}

	/**
	 * 转为float
	 * 
	 * @param obj
	 *            输入对象
	 * @param defaultValue
	 *            默认值
	 * @return double，如果obj不是数值默认返回默认值
	 */
	public static float toFloat(Object obj, float defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		if (obj instanceof Number) {
			return ((Number) obj).floatValue();
		}
		return NumberUtils.toFloat(obj.toString(), defaultValue);
	}

	/**
	 * 转为double
	 * 
	 * @param obj
	 *            输入对象
	 * @return double，如果obj不是数值默认返回0
	 */
	public static double toDouble(Object obj) {
		return toDouble(obj, 0);
	}

	/**
	 * 转为double
	 * 
	 * @param obj
	 *            输入对象
	 * @param defaultValue
	 *            默认值
	 * @return double，如果obj不是数值默认返回默认值
	 */
	public static double toDouble(Object obj, double defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		if (obj instanceof Number) {
			return ((Number) obj).doubleValue();
		}
		return NumberUtils.toDouble(obj.toString(), defaultValue);
	}

	/**
	 * 转为String
	 * 
	 * @param obj
	 *            输入对象
	 * @return 字符串
	 */
	public static String toString(Object obj) {
		return toString(obj, StringUtils.EMPTY);
	}

	/**
	 * 转为String
	 * 
	 * @param obj
	 * @param defaultValue
	 *            默认字符串
	 * @return 如果obj是null返回默认字符串
	 */
	public static String toString(Object obj, String defaultValue) {
		try{
			if (obj == null) {
				return defaultValue;
			}

			String value = null;
			if (obj instanceof String) {
				return (String) obj;
			} else if (obj instanceof Date) {
				value = DateUtil.date2Str((Date) obj);
			} else {
				value = obj.toString();
			}
			return value == null ? defaultValue : value;
		}catch (Exception ex){
			return defaultValue;
		}

	}
	public static boolean toBoolean(Object obj) {
		return toBoolean(obj,false);
	}

	public static boolean toBoolean(Object obj, boolean defaultValue) {
		try{
			return Boolean.valueOf(obj.toString());
		}catch (Exception ex){
			return defaultValue;
		}
	}
	
	/**
	 * 对象克隆
	 * 
	 * @param src
	 * @return
	 */
	public static Object deepClone(Object src) {
		Object o = null;
		try {
			if (src != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(src);
				oos.close();

				ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
				ObjectInputStream ois = new ObjectInputStream(bais);
				o = ois.readObject();
				ois.close();
			}
		} catch (IOException e) {
			logger.error("", e);
		} catch (ClassNotFoundException e) {
			logger.error("", e);
		}
		return o;
	}

	@SuppressWarnings("unchecked")
	public static <T> T deepClone(Object src, Class<T> t) {
		Object o = deepClone(src);
		if (o != null) {
			return (T) o;
		}
		return null;
	}

}
