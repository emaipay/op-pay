package com.hitler.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hitler.core.repository.GenericRepository;
import com.hitler.dto.user.PayRolePermissionDTO;
import com.hitler.entity.PayRolePermission;
import com.hitler.repository.IPayRolePermissionRepository;
import com.hitler.repository.IPayRoleRepository;
import com.hitler.service.IPayRolePermissionService;
import com.hitler.service.IPayUserService;
import com.hitler.service.support.GenericService;

/**
 * 权限资源管理 实现
 * 
 * @author klp
 * @version 创建时间：2017年5月4日 下午2:06:21
 */
@Service
public class PayRolePermissionService extends GenericService<PayRolePermission, Integer>
		implements IPayRolePermissionService {

	@Resource
	private IPayRolePermissionRepository repository;

	@Resource
	private IPayRoleRepository payRoleRepository;

	@Resource
	private IPayRolePermissionRepository payRolePermissionRepository;
	@Resource
	private IPayUserService payUserService;

	public PayRolePermissionService() {
		super(PayRolePermission.class);
	}

	@Override
	protected GenericRepository<PayRolePermission, Integer> getRepository() {
		return repository;
	}

	@Override
	public List<PayRolePermissionDTO> getRolePermissions(Integer roleId) {
		
		List<PayRolePermissionDTO> rolePermissionList = payRolePermissionRepository.getRolePermissions(roleId);
			
		return rolePermissionList;
	}

}
