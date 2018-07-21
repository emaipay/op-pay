package com.hitler.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayPlatformType;
import com.hitler.repository.IPayPlatformTypeRepository;
import com.hitler.service.IPayPlatformTypeService;
import com.hitler.service.support.GenericService;

/**
 * 支付类型实现
 * 
 * @author klp
 *
 */
@Service
public class PayPlatformTypeService extends GenericService<PayPlatformType, Integer>
		implements IPayPlatformTypeService {

	@Resource
	private IPayPlatformTypeRepository repository;

	public PayPlatformTypeService() {
		super(PayPlatformType.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected GenericRepository<PayPlatformType, Integer> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteByIdList(List<Integer> ids) {
	 repository.deleteByIdList(ids);
		
	}
 
}
