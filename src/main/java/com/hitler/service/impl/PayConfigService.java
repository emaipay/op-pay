package com.hitler.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayConfig;
import com.hitler.entity.PayConfig.MethodType;
import com.hitler.repository.IPayConfigRepository;
import com.hitler.service.IPayConfigService;
import com.hitler.service.support.GenericService;

/**
 *  各支付平台配置实现
 * @author klp
 *
 */
@Service
public class PayConfigService extends GenericService<PayConfig, Integer> implements IPayConfigService {
	
	
	@Resource
	private IPayConfigRepository repository;

	public PayConfigService() {
		super(PayConfig.class);
	}

	@Override
	protected GenericRepository<PayConfig, Integer> getRepository() {
		return repository;
	}

	@Override
	public PayConfig queryConfigByType(String type, MethodType methodType) {
		return repository.queryConfigByType(type, methodType);
	}

	
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteByIdList(List<Integer> ids) {
		// TODO Auto-generated method stub
		repository.deleteByIdList(ids);
	}

}
