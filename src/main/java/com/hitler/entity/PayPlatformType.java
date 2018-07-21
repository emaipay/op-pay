package com.hitler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hitler.core.entity.CheckableEntity;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_pay_platform_type")
public class PayPlatformType extends CheckableEntity<Integer> {

 
	/**
	 * 
	 */
	private static final long serialVersionUID = 4956458285882263883L;

	@Column(name = "platform_type", columnDefinition = "varchar(10) COMMENT '支付平台类型'", nullable = false)
	private String platformType;

	@Column(name = "TYPE_NAME", columnDefinition = "varchar(10) COMMENT '支付平台名称'", nullable = false)
	private String typeName;

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
