package com.hitler.core.entity;

import java.io.Serializable;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import com.hitler.core.entity.listener.CheckingEntityListener;

@MappedSuperclass
@EntityListeners(CheckingEntityListener.class)
public abstract class CheckableEntity<PK extends Serializable> extends AuditableEntity<PK> {

	private static final long serialVersionUID = 2469813719494517116L;


}
