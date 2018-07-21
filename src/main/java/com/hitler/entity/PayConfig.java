package com.hitler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import com.hitler.core.entity.CheckableEntity;
import com.hitler.core.enums.PersistEnum;
import com.hitler.entity.PayUser.AccountStatus;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_pay_config")
public class PayConfig extends CheckableEntity<Integer> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3576109131321351677L;
	@Column(name = "platform_type", columnDefinition = "varchar(10) comment '支付类型'",nullable = false)
	private String platformType;
	@Column(name = "class_path", columnDefinition = "varchar(200) comment '类路径'",nullable = false)
	private String classPath;
	@Column(name = "class_method", columnDefinition = "varchar(50) comment '方法名'",nullable = false)
	private String classMethod;
	/**
	 * 方法类型
	 */
	@Type(type = "com.hitler.core.enums.MyEnumType")
	@Column(name = "METHOD_TYPE",columnDefinition="tinyint comment '方法类型'",nullable=false)
	private MethodType methodType;

	public enum MethodType implements PersistEnum<AccountStatus> {
		数据组装("0"), 单号获取("1"), 校验回调("2"),订单查询("3");
		private final String value;

		MethodType(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return value;
		}
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public String getClassMethod() {
		return classMethod;
	}

	public void setClassMethod(String classMethod) {
		this.classMethod = classMethod;
	}

	public MethodType getMethodType() {
		return methodType;
	}

	public void setMethodType(MethodType methodType) {
		this.methodType = methodType;
	}
	
	
}
