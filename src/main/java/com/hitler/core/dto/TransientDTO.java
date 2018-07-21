package com.hitler.core.dto;

import java.io.Serializable;

import com.hitler.core.dto.support.GenericDTO;

/**
 * 所有XXXCreateDTO必须继承此DTO,无租户代号则为空,不影响save
 * @author createDTO通用对象
 * 瞬态数据传输对象
 */
public abstract class TransientDTO<PK extends Serializable> extends GenericDTO<PK> {

	private static final long serialVersionUID = 6693846907130500855L;

	private PK id;


	public void setId(PK id) {
		this.id = id;
	}

	@Override
	public PK getId() {
		return id;
	}

}
