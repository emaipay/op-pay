package com.hitler.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayTenantMerchantBank;
import com.hitler.repository.IPayTenantMerchantBankRepository;
import com.hitler.service.IPayTenantMerchantBankService;
import com.hitler.service.support.GenericService;

/**
 * 接入商户对应商户的支付方式(银行卡) 实现
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 下午4:09:58
 */
@Service
public class PayTenantMerchantBankService extends GenericService<PayTenantMerchantBank, Integer>
		implements IPayTenantMerchantBankService {

	@Resource
	private IPayTenantMerchantBankRepository repository;

	public PayTenantMerchantBankService() {
		super(PayTenantMerchantBank.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected GenericRepository<PayTenantMerchantBank, Integer> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	public List<PayTenantMerchantBank> queryPayConnMerchantBank(Integer merchantId, Integer connId) {
		return repository.queryPayBankList(connId, merchantId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteByIdList(List<Integer> idList) {
		// TODO Auto-generated method stub
		repository.deleteByIdList(idList);
	}

	@Override
	public List<PayTenantMerchantBank> findByTenantId(Integer tenantId) {
		// TODO Auto-generated method stub
		return repository.findByTenantId(tenantId);
	}

	@Override
	@javax.transaction.Transactional
	public void deleteByTenantIdId(Integer tenantId) throws Exception {
		repository.deleteByTenantIdId(tenantId);
		
	}

	@Override
	public List<Integer> queryPayBankIdList(Integer connId, Integer merchantId) {
		return repository.queryPayBankIdList(connId, merchantId);
	}

	 

}
