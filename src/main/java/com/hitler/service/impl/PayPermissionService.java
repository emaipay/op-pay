package com.hitler.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hitler.core.repository.GenericRepository;
import com.hitler.core.utils.StringUtils;
import com.hitler.dto.user.NavMenuDTO;
import com.hitler.dto.user.PayPermissionDTO;
import com.hitler.dto.user.PayPermissionTreeDTO;
import com.hitler.dto.user.PayRolePermissionDTO;
import com.hitler.entity.PayPermission;
import com.hitler.entity.PayRole;
import com.hitler.entity.PayRolePermission;
import com.hitler.repository.IPayPermissionRepository;
import com.hitler.repository.IPayRolePermissionRepository;
import com.hitler.repository.IPayRoleRepository;
import com.hitler.service.IPayPermissionService;
import com.hitler.service.IPayRolePermissionService;
import com.hitler.service.IPayUserService;
import com.hitler.service.support.GenericService;

/**
 * 权限服务实现类
 * 
 * @author jtwise 2017年3月20日下午3:31:05
 * @version
 */

@Service
public class PayPermissionService extends GenericService<PayPermission, Integer> implements IPayPermissionService {

	@Resource
	private IPayPermissionRepository repository;
	@Resource
	private IPayRolePermissionRepository rolePermissionRepository;
	@Resource
	private IPayRoleRepository roleRepository;

	@Resource
	private IPayUserService payUserService;

	@Resource
	private IPayRolePermissionService payRolePermissionService;

	public PayPermissionService() {
		super(PayPermission.class);
	}

	public PayPermissionService(Class<PayPermission> entityClass) {
		super(entityClass);
	}

	@Override
	protected GenericRepository<PayPermission, Integer> getRepository() {
		return repository;
	}

	@Override
	public PayPermission findByPath(String path) {
		return null;
	}

	@Override
	public Set<String> findUserPermissionByUID(Long userId) {
		return null;
	}

	@Override
	public List<PayPermissionDTO> findByRoleId(Integer roleId) throws Exception {
		PayRole r = roleRepository.findOne(roleId);
		return repository.findByRoleId(r);
	}

	@Override
	public List<PayPermission> findByUserId(Long userId) throws Exception {
		return null;
	}

	@Override
	public boolean insertRolePermission(Integer roleId, Integer permissionId) throws Exception {
		return false;
	}

	@Override
	public boolean deleteRolePermission(Integer roleId, Integer permissionId) throws Exception {
		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteByRoleId(Integer roleId) throws Exception {
		rolePermissionRepository.deleteByRoleId(roleId);
		roleRepository.delete(roleId);
		return false;
	}

	/**
	 * 权限分配保存
	 * 
	 * @param roleId
	 * @param permissions
	 * @throws Exception
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void permissionSave(Integer roleId, String permissions) throws Exception {
		List<PayRolePermission> rolePermissionList = new ArrayList<>();
		PayRolePermission rolePermission;
		PayRole role = roleRepository.findOne(roleId);
		if (role != null && !StringUtils.isEmpty(permissions)) {
			for (String p : permissions.split(",")) {
				rolePermission = new PayRolePermission();
				rolePermission.setRoleId(role);
				PayPermission permissionId = repository.findOne(Integer.parseInt(p));
				if (permissionId != null) {
					rolePermission.setPermissionId(permissionId);
					rolePermissionList.add(rolePermission);
				}
			}
		}
		// 清空记录再插入
		rolePermissionRepository.deleteByRoleId(roleId);
		rolePermissionRepository.save(rolePermissionList);
	}

	@Override
	public PayPermission findByCode(String code) {
		return repository.findByCode(code);
	}

	@Override
	public List<PayPermissionTreeDTO> permissionTree() {
		List<PayPermission> list = repository.findPermissionTree();
		List<PayPermissionTreeDTO> permissionTreeDTOList = Lists.newArrayList();
		generatePermissionTree(list, permissionTreeDTOList, 0);
		return permissionTreeDTOList;
	}

	@Override
	public List<PayPermission> findByCodeIn(List<String> codes) {
		return null;
	}

	@Override
	public List<PayPermission> findBySpec(Integer id) {
		return null;
	}

	private List<PayPermissionTreeDTO> generatePermissionTree(List<PayPermission> allList, List<PayPermissionTreeDTO> dtoList,
			Integer parentPermissionId) {
		if (allList == null || allList.size() == 0)
			return dtoList;
		for (PayPermission p : allList) {
			if (p.getParentPermissionId() == parentPermissionId) {
				PayPermissionTreeDTO pt = new PayPermissionTreeDTO();
				pt.setId(p.getId());
				pt.setText(p.getPermissionName());
				pt.setDeep(p.getDeep());
				// 递归查找子权限
				List<PayPermissionTreeDTO> subTreeList = generatePermissionTree(allList,
						new ArrayList<PayPermissionTreeDTO>(), p.getId());
				pt.setChildren(subTreeList);

				dtoList.add(pt);
			}
		}

		return dtoList;

	}

	/**
	 * 权限:根据用户角色id集合获取权限(去重) getMenuPermissionList
	 */
	@Override
	public Set<String> getPermissionSet(Integer userId) {
		// 1-去重
		Collection<PayRole> roleIdList = payUserService.getRolesByUserId(userId);
		Set<String> permissionSet = Sets.newHashSet();
		for (PayRole role : roleIdList) {
			List<PayRolePermissionDTO> rolePermissionList = payRolePermissionService.getRolePermissions(role.getId());
			if (rolePermissionList != null && rolePermissionList.size() > 0) {
				for (PayRolePermissionDTO rpDTO : rolePermissionList) {
					if (!permissionSet.contains(rpDTO.getCode())) {
						permissionSet.add(rpDTO.getCode());
					}
				}
			}
		}
		return permissionSet;
	}

	@Override
	public List<NavMenuDTO> getMenuPermissionList(Integer userId) {
		// 1-去重
		Collection<PayRole> roleIdList = payUserService.getRolesByUserId(userId);
		List<PayRolePermissionDTO> menuPermissionList = Lists.newArrayList();
		Set<Integer> permissionIds=new HashSet<Integer>();
		for (PayRole role : roleIdList) {
			List<PayRolePermissionDTO> rolePermissionList = payRolePermissionService.getRolePermissions(role.getId());
			if (rolePermissionList != null && rolePermissionList.size() > 0) {
				for (PayRolePermissionDTO rpDTO : rolePermissionList) {
					if(permissionIds.contains(rpDTO.getPermissionId())){
						continue;
					}
					permissionIds.add(rpDTO.getPermissionId());
					if (!menuPermissionList.contains(rpDTO)
							&& rpDTO.getPermissionType().equals(PayPermission.PermissionType.菜单)
							&& rpDTO.getFloor() > 0) {
						menuPermissionList.add(rpDTO);
					}
				}
			}
		}
		// 2-组装成树形数据
		List<NavMenuDTO> treeList = Lists.newArrayList();
		if (menuPermissionList != null && menuPermissionList.size() > 0) {
			generatePermissionTree(menuPermissionList, treeList, 1);
		}
		return treeList;
	}
	/**
	 * 递归组装树形数据
	 * @param list
	 * @param treeList
	 * @param parentPermissionId
     * @return
     */
	private List<NavMenuDTO> generatePermissionTree(List<PayRolePermissionDTO> list, List<NavMenuDTO> treeList, int parentPermissionId){
		if(list == null || list.size() == 0)
			return treeList;
		for (PayRolePermissionDTO r : list) {
			if(r.getParentPermissionId()==parentPermissionId) {
				NavMenuDTO navMenu = new NavMenuDTO();
				navMenu.setId(r.getDeep());
				navMenu.setPermissionName(r.getPermissionName());
				navMenu.setIcon(r.getIcon());
				navMenu.setPath(r.getPath());
				// 递归查找子权限
				List<NavMenuDTO> subTreeList = generatePermissionTree(list, Lists.newArrayList(), r.getPermissionId());
				navMenu.setChildren(subTreeList);
				treeList.add(navMenu);
			}
		}
		return treeList;
	}
}
