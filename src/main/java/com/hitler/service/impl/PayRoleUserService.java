package com.hitler.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayRoleUser;
import com.hitler.repository.IPayRoleUserRepository;
import com.hitler.service.IPayRoleUserService;
import com.hitler.service.support.GenericService;

/**
 * 角色用户服务实现
 *
 * @author Kylin
 */
@Service
public class PayRoleUserService extends GenericService<PayRoleUser, Integer> implements IPayRoleUserService {

    @Resource
    private IPayRoleUserRepository repository;

    public PayRoleUserService() {
        super(PayRoleUser.class);
    }


    @Override
    protected GenericRepository<PayRoleUser, Integer> getRepository() {
        return repository;
    }

    @Override
    public int deleteByUserId(Integer userId) {
        return repository.deleteByUserId(userId);
    }

    @Override
    public List<PayRoleUser> findByUserId(Integer userId) {
        return repository.findByUserId(userId);
    }


	@Override
	public List<PayRoleUser> findByRoleId(Integer roleId) {
		return repository.findByRoleId(roleId);
	}
}
