package com.hitler.core.utils;

import java.util.Collection;
import java.util.List;

/**
 * 集合工具类
 * 
 * @author chillming
 */
public class CollectionHelper {
	/**
	 * 判断一个集合对象是否为null或为空
	 * 
	 * @param collection
	 *            要检测的集合对象
	 * @return 若集合对象为null或为空，则返回true；否则返回false
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * 判断一个集合对象是否不为null且不为空
	 * 
	 * @param collection
	 *            要检测的集合对象
	 * @return 若集合对象不为null且不为空，则返回true；否则返回false
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return !CollectionHelper.isEmpty(collection);
	}

	/**
	 * 得到集合的第一个元素
	 * 
	 * @param <T>
	 *            泛型
	 * @param collection
	 *            集合
	 * @return 集合的第一个元素，若集合为null，则返回null
	 */
	public static <T> T getFirst(Collection<T> collection) {
		return isEmpty(collection) ? null : collection.iterator().next();
	}
	
	public void castList(List<?> source,List<?> target){
		if(isNotEmpty(source)){
			
		}
	}

}
