package com.hitler.dto.user;

import com.hitler.core.dto.support.PersistentDTO;

/**
 * 角色管理 Dto Thu May 04 11:10:13 CST 2017 klp
 */
public class PayRoleUpdateDTO extends PersistentDTO<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4740108361723455969L;

	 private Integer id;


	    /**
	     * 名称
	     */
	    private String roleName;


		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getRoleName() {
			return roleName;
		}

		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}

	    

}
