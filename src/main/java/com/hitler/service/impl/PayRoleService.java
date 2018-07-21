package com.hitler.service.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;
import com.hitler.core.repository.GenericRepository;
import com.hitler.dto.user.PayRoleDTO;
import com.hitler.dto.user.PayRolePermissionDTO;
import com.hitler.entity.PayRole;
import com.hitler.entity.PayRoleUser;
import com.hitler.entity.PayUser;
import com.hitler.repository.IPayRoleRepository;
import com.hitler.repository.IPayRoleUserRepository;
import com.hitler.service.IPayRolePermissionService;
import com.hitler.service.IPayRoleService;
import com.hitler.service.IPayUserService;
import com.hitler.service.support.GenericService;

/**
 * 角色服务实现类
 * 
 * @author jtwise 2017年3月20日下午3:31:15
 * @version
 */
@Service
public class PayRoleService extends GenericService<PayRole, Integer> implements IPayRoleService {

	@Resource
	private IPayRoleRepository repository;
	@Resource
	private IPayRoleUserRepository roleUserRepository;
	@Resource
	private IPayUserService payUserService;
	@Resource
	private IPayRolePermissionService payRolePermissionService;

	public PayRoleService() {
		super(PayRole.class);
	}

	public PayRoleService(Class<PayRole> entityClass) {
		super(entityClass);
	}

	@Override
	protected GenericRepository<PayRole, Integer> getRepository() {
		return repository;
	}

	@Override
	public List<PayRoleDTO> getRolesByUserId(Integer userId) {
		return roleUserRepository.getRolesByUserId(userId);
	}

	@Override
	public PayRole findByRoleName(String roleName) {
		return repository.findByRoleName(roleName);
	}

	/**
	 * 权限:根据用户角色id集合获取权限(去重) getMenuPermissionList
	 */
	public Set<String> getRoleSet(Integer userId) {
		// 1-去重
		Collection<PayRole> roleIdList = payUserService.getRolesByUserId(userId);
		Set<String> roleSet = Sets.newHashSet();
		for (PayRole role : roleIdList) {
			List<PayRolePermissionDTO> rolePermissionList = payRolePermissionService.getRolePermissions(role.getId());
			if (rolePermissionList != null && rolePermissionList.size() > 0) {
				for (PayRolePermissionDTO rpDTO : rolePermissionList) {
					if (!roleSet.contains(rpDTO.getRoleName())) {
						roleSet.add(rpDTO.getRoleName());
					}
				}
			}
		}
		return roleSet;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void distributionRoleSave(String userIds, Integer[] roleIds) throws Exception {
		String[] strIds = userIds.split(",");
		Integer[] intIds = new Integer[strIds.length];
		for (int i = 0; i < strIds.length; i++) {
			intIds[i] = Integer.parseInt(strIds[i]);
		}

		List<PayUser> userList = payUserService.findByIdIn(Arrays.asList(intIds));

		Collection<PayRoleUser> ruLists = new HashSet<PayRoleUser>();
		for (PayUser user : userList) {
			if (user == null) {
				new Exception("user不存在！");
			}
			roleUserRepository.deleteByUserId(user.getId());
			if (roleIds.length > 0) {
				List<PayRole> roleList = repository.findByIdIn(Arrays.asList(roleIds));
				for (PayRole r : roleList) {
					PayRoleUser ru = new PayRoleUser();
					ru.setUserId(user);
					ru.setRoleId(r);
					ruLists.add(ru);
				}
			}
		}
		roleUserRepository.save(ruLists);
	}

}
