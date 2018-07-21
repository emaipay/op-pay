package com.hitler.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayTenantMerchant;
import com.hitler.repository.IPayTenantMerchantRepository;
import com.hitler.service.IPayTenantMerchantService;
import com.hitler.service.support.GenericService;

/**
 * 接入商户对应第三方支付商户实现
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 下午3:16:48
 */
@Service
public class PayTenantMerchantService extends GenericService<PayTenantMerchant, Integer>
		implements IPayTenantMerchantService {

	@Resource
	private IPayTenantMerchantRepository repository;

	public PayTenantMerchantService() {
		super(PayTenantMerchant.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected GenericRepository<PayTenantMerchant, Integer> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	
	@Override
	public List<PayTenantMerchant> queryPayMerchant(String memberId, String terminalId) {
		List<PayTenantMerchant> conList = repository.queryPayConnMerchant(memberId, terminalId);
		return conList;
	}

	@Override
	public List<PayTenantMerchant> findByTenantId(Integer tenantId) {
		// TODO Auto-generated method stub
		return repository.findByTenantId(tenantId);
	}

	@Override
	@Transactional
	public void deleteByTenantId(Integer tenantId) {
		repository.deletetenantId(tenantId);
		
	}

	 

}
