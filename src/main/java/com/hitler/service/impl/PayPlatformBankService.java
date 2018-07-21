package com.hitler.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayPlatformBank;
import com.hitler.repository.IPayPlatformBankRepository;
import com.hitler.service.IPayPlatformBankService;
import com.hitler.service.support.GenericService;

/**
  * 第三方支付平台对应支付方式配置实现
 * @author klp
 * @version 创建时间：2017年5月3日 下午2:17:09 
 */
@Service
public class PayPlatformBankService extends GenericService<PayPlatformBank, Integer>
		implements IPayPlatformBankService {

	@Resource
	private IPayPlatformBankRepository repository;

	public PayPlatformBankService() {
		super(PayPlatformBank.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected GenericRepository<PayPlatformBank, Integer> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteByIdList(List<Integer> idList) {
		// TODO Auto-generated method stub
		repository.deleteByIdList(idList);
	}

	@Override
	public List<PayPlatformBank> queryByPlatformId(Integer payPlatformId) {
		return repository.queryByPlatformId(payPlatformId);
	}

 

}
