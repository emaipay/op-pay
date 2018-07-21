package com.hitler.dto.user;

import com.hitler.core.dto.TransientDTO;

/**
 * 权限资源管理 Dto Thu May 04 11:32:09 CST 2017 klp
 */
public class PayRolePermissionCreateDTO extends TransientDTO<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7647277163780106439L;

	private Integer id;

	private Integer roleId;

	private Integer permissionId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

}
