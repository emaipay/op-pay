package com.hitler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hitler.core.entity.CheckableEntity;
@Entity
@Table(name="t_pay_preference")
@DynamicInsert
@DynamicUpdate
public class PayPreference extends CheckableEntity<Integer> {

	private static final long serialVersionUID = 8531849939019140335L;
	@Column(name = "code", columnDefinition = "varchar(50) 常量code")
	private String code;
	@Column(name = "name", columnDefinition = "varchar(20) 常量名称")
	private String name;
	@Column(name = "value", columnDefinition = "varchar(100) 常量值")
	private String value;
	@Column(name = "remark", columnDefinition = "varchar(100) 备注")
	private String remark;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
