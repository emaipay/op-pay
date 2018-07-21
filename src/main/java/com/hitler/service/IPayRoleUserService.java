package com.hitler.service;


import java.util.List;

import com.hitler.entity.PayRoleUser;
import com.hitler.service.support.IGenericService;

/**
 * 权限分配service层
 * @author klp
 *
 */
public interface IPayRoleUserService extends IGenericService<PayRoleUser, Integer> {

    public int deleteByUserId(Integer userId);

    public List<PayRoleUser> findByUserId(Integer userId);

	public List<PayRoleUser> findByRoleId(Integer roleId);
}
