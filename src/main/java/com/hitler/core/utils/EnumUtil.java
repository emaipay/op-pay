package com.hitler.core.utils;

import java.util.HashMap;
import java.util.Map;

import com.hitler.core.enums.PersistEnum;

/**
 * 枚举工具类， 枚举类必须要继承PersistEnum。
 * @since 2017-03-16
 */
public class EnumUtil {

	/**
	 * key: 包名_value。如：com.hitler.entity.bet.BetOrder$BetWay_1
	 * value: 枚举对象
	 */
	private static Map<String, PersistEnum<Enum<?>>> map = new HashMap<String, PersistEnum<Enum<?>>>();
	
	@SuppressWarnings("unchecked")
	public static PersistEnum<Enum<?>> get(Class<?> clazz, String value) {
		if(value == null || "".equals(value))
			return null;
		
		String key = clazz.getName() + "_" + value;
		PersistEnum<Enum<?>> pe = null;
		
		if(map.containsKey(key)) {
			pe = map.get(key);
		} else if(map.containsKey(clazz.getName())) {
			
		} else {
			Object[] oo = clazz.getEnumConstants();
			for (Object e : oo) {
				PersistEnum<Enum<?>> _p = (PersistEnum<Enum<?>>)e;
    			map.put(clazz.getName() + "_" + _p.getValue(), _p);
			}
			map.put(clazz.getName(), null);
			
			pe = map.get(key);
		}
			
		return pe;
	}
	
	public static String getStr(Class<?> clazz, String value) {
		PersistEnum<Enum<?>> p = get(clazz, value);
		return p == null ? null : p.getName();
	}
	
	
}
