package com.hitler.core.dto.support;

import java.io.Serializable;


/**
 * @author Lane
 *
 * 数据传输对象抽象类
 *
 */
public abstract class GenericDTO<PK extends Serializable> implements IGenericDTO<PK> {

	private static final long serialVersionUID = 2572279813924674692L;

}
