package com.hitler.service;

import java.util.List;

import com.hitler.dto.user.PayRolePermissionDTO;
import com.hitler.entity.PayRolePermission;
import com.hitler.service.support.IGenericService;

/**
* 权限资源管理service层
* @author klp
* @version 创建时间：2017年5月4日 下午1:42:47
*/
public interface IPayRolePermissionService extends IGenericService<PayRolePermission, Integer> {
	
	List<PayRolePermissionDTO> getRolePermissions(Integer roleId);

}
