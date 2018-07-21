package com.hitler.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayTenantLimit;
import com.hitler.repository.IPayTenantLimitRepository;
import com.hitler.service.IPayTenantLimitService;
import com.hitler.service.support.GenericService;

/**
 * 接入商户第三方支付限额 实现
 * @author klp
 *
 */
@Service
public class PayTenantLimitService extends GenericService<PayTenantLimit, Integer> implements IPayTenantLimitService {

	@Resource
	private IPayTenantLimitRepository repository;
	

	public PayTenantLimitService() {
		super(PayTenantLimit.class);
	}

	@Override
	public PayTenantLimit queryPayConnLimt(Integer connId, Integer platformId) {
		return repository.queryConnLimit(connId, platformId);
	}

	@Override
	protected GenericRepository<PayTenantLimit, Integer> getRepository() {
		return repository;
	}

	@Override
	public List<PayTenantLimit> queryConnLimitByTenantId(Integer tenantId) {
		// TODO Auto-generated method stub
		return repository.queryConnLimitByTenantId(tenantId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteByTenantId(Integer tenantId) {
		repository.deleteByTenantId(tenantId);
	}

}
