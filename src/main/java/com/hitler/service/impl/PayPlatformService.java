package com.hitler.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayPlatform;
import com.hitler.entity.PayPlatformBank;
import com.hitler.repository.IPayPlatformRepository;
import com.hitler.service.IPayPlatformService;
import com.hitler.service.support.GenericService;

/**
 *  支付平台服务实现
 * @author klp
 *
 */
@Service
public class PayPlatformService extends GenericService<PayPlatform, Integer> implements IPayPlatformService {

	public PayPlatformService() {
		super(PayPlatform.class);
	}

	@Resource
	private IPayPlatformRepository repository;

	@Override
	protected GenericRepository<PayPlatform, Integer> getRepository() {
		return repository;
	}

	@Override
	public PayPlatformBank queryPayPlatformBankCode(Integer bankId, Integer platformId) {
		// TODO Auto-generated method stub
		return 	  repository.queryPayPlatformBankCode(bankId, platformId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteByIdList(List<Integer> ids) {
		// TODO Auto-generated method stub
		repository.deleteByIdList(ids);
	}

	 
	
	
	

}
