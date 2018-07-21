package com.hitler.core.enums;

/**
 * 需要持久化的enum类，都需要实现的接口
 *
 * @author Kylin
 * 2015-7-23 下午4:11:04
 * @param <E>
 */
public interface PersistEnum<E extends Enum<?>> {
	
    String getValue();
    
    default String getName(){
    	Enum<?> e = (Enum<?>)this;
    	return e.name();
    }
}
