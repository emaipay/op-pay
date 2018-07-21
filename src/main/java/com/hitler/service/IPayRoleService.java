package com.hitler.service;

import java.util.List;
import java.util.Set;

import com.hitler.dto.user.PayRoleDTO;
import com.hitler.entity.PayRole;
import com.hitler.service.support.IGenericService;

/**
 * 角色管理service层
 * @author klp
 *
 */
public interface IPayRoleService extends IGenericService<PayRole, Integer> {


   List<PayRoleDTO> getRolesByUserId(Integer userId);

   PayRole findByRoleName(String roleName);
   
   public Set<String> getRoleSet(Integer userId);
   public void distributionRoleSave(String userIds, Integer[] roleIds) throws Exception;

}