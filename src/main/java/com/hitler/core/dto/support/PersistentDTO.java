package com.hitler.core.dto.support;

import java.io.Serializable;

/**
 * @author User
 * 持久数据传输对象
 */
public abstract class PersistentDTO<PK extends Serializable> extends GenericDTO<PK> {

	private static final long serialVersionUID = -7604988709496150282L;
	
	private PK id;

	public void setId(PK id) {
		this.id = id;
	}

	@Override
	public PK getId() {
		return id;
	}

}
