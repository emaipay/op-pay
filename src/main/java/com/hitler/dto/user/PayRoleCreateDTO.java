package com.hitler.dto.user;

import com.hitler.core.dto.TransientDTO;

/**
 * 角色管理 Dto Thu May 04 11:10:13 CST 2017 klp
 */
public class PayRoleCreateDTO extends TransientDTO<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3886841035021121445L;

	 /**
     * 名称
     */
    private String roleName;


	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

    
    

}
