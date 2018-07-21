package com.hitler.service;

import java.util.List;
import java.util.Set;

import com.hitler.dto.user.NavMenuDTO;
import com.hitler.dto.user.PayPermissionDTO;
import com.hitler.dto.user.PayPermissionTreeDTO;
import com.hitler.entity.PayPermission;
import com.hitler.service.support.IGenericService;

/**
 * 
 * @author 
 *
 */
public interface IPayPermissionService extends IGenericService<PayPermission, Integer> {
    PayPermission findByPath(String path);

    public Set<String> findUserPermissionByUID(Long userId);
    /**
     * 通过角色查找对应权限
     * @param roleId
     * @return
     * @throws Exception
     */
    public List<PayPermissionDTO> findByRoleId(Integer roleId) throws Exception;

    /**
     * 通过用户id查找对应权限
     * @param userId
     * @return
     * @throws Exception
     */
    public List<PayPermission> findByUserId(Long userId) throws Exception;
    /**
     * 创建角色-权限关系
     * @param roleId
     * @param permissionId
     * @return
     * @throws Exception
     */
    public boolean insertRolePermission(Integer roleId, Integer permissionId) throws Exception;
    /**
     * 删除角色-权限关系
     * @param roleId
     * @param permissionId
     * @return
     * @throws Exception
     */
    public boolean deleteRolePermission(Integer roleId, Integer permissionId) throws Exception;

    public boolean deleteByRoleId(Integer roleId) throws Exception;

    /**
     * 权限分配
     * @param roleId
     * @param permissions
     * @return
     * @throws Exception
     */
    public void permissionSave(Integer roleId, String permissions) throws Exception;

    public PayPermission findByCode(String code);

    /**
     * 权限树
     */
    public List<PayPermissionTreeDTO> permissionTree();
    /**
     * code集合查询
     * @param codes
     * @return
     */
    public List<PayPermission> findByCodeIn(List<String> codes);
    /**
     * 查询所有该节点下的子权限
     * @param id
     * @return
     */
    public List<PayPermission> findBySpec(Integer id);
    /**
	 * 权限:根据用户角色id集合获取权限(去重)
	 * getMenuPermissionList
	 */
    public Set<String> getPermissionSet(Integer userId) ;
    
    /**
     * 根据用户id获取用户菜单
     * @param userId
     * @return
     */
    public List<NavMenuDTO> getMenuPermissionList(Integer userId);
}