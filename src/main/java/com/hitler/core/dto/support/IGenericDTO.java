package com.hitler.core.dto.support;

import java.io.Serializable;

/**
 * @author Lane
 *
 * 数据传输对象接口
 *
 */
public interface IGenericDTO<PK extends Serializable> extends Serializable {

	PK getId();
	
}
