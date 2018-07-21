package com.hitler.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hitler.core.entity.PersistableEntity;
import com.hitler.core.entity.annotion.Checked;

/**
 * 角色类
 * 
 * @author onsoul
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_pay_role_permission")
public class PayRolePermission extends PersistableEntity<Integer> {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@Checked
	@ManyToOne
	@JoinColumn(name = "ROLE_ID", referencedColumnName = "id", columnDefinition="INT COMMENT '角色ID'", nullable = false)
	private PayRole roleId;
	
	

	/**
	 * 权限Id
	 */
	@Checked
	@ManyToOne
	@JoinColumn(name = "PERMISSION_ID", referencedColumnName = "id", columnDefinition="INT COMMENT '权限Id'", nullable = false)
	private PayPermission permissionId;

	public PayRolePermission() {
	}




	public PayRole getRoleId() {
		return roleId;
	}

	public void setRoleId(PayRole roleId) {
		this.roleId = roleId;
	}

	public PayPermission getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(PayPermission permissionId) {
		this.permissionId = permissionId;
	}
}
