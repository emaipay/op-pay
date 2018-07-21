package com.hitler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hitler.core.entity.CheckableEntity;

/**
 * @author 第三方支付平台
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_pay_platform_config")
public class PayPlatformConfig extends CheckableEntity<Integer> {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 644063055125748925L;

	@Column(name = "platform_NAME", columnDefinition = "varchar(10) COMMENT '支付平台名称'", nullable = false)
	private String platformName;

	@Column(name = "class_path", columnDefinition = "varchar(200) COMMENT '类路径'", nullable = false)
	private String classPath;

	@Column(name = "platform_code", columnDefinition = "varchar(15) COMMENT '支付类型code'", nullable = false)
	private String platformCode;

	@Column(name = "platform_id", columnDefinition = "int(11) comment '支付类型id'",nullable = false)
	private String platformId;

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	
}
