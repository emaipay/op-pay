package com.hitler.dto.user;

import com.hitler.core.dto.TransientDTO;

/**
 * 权限分配 Dto Thu May 04 11:50:00 CST 2017 klp
 */
public class PayRoleUserUpdateDTO extends TransientDTO<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4682000446841055616L;

	private Integer userId;

	private Integer roleId;

	private Integer id;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
