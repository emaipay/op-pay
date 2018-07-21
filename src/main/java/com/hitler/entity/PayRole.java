package com.hitler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hitler.core.entity.CheckableEntity;

/**
 * 角色类
 * 
 * @author onsoul
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_pay_role")
public class PayRole extends CheckableEntity<Integer> {

	private static final long serialVersionUID = 1L;



	@Column(name = "ROLE_NAME", length = 32, columnDefinition = "varchar(20)",nullable = true)
	private String roleName; // 名称
	
//	@Column(name = "TENANT_CODE", length = 10, columnDefinition = "varchar(20) COMMENT '租户代号'", nullable = true)
//	private String tenantCode;
	
	
	

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

//	public String getTenantCode() {
//		return tenantCode;
//	}
//
//	public void setTenantCode(String tenantCode) {
//		this.tenantCode = tenantCode;
//	}
	
}
