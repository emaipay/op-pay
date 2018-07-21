package com.hitler.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayPreference;
import com.hitler.repository.IPayPreferenceRepository;
import com.hitler.service.IPayPreferenceService;
import com.hitler.service.support.GenericService;

/**
 * 用户权限 实现
 * @author klp
 *
 */
@Service
public class PayPreferenceService extends GenericService<PayPreference, Integer> implements IPayPreferenceService {

	public PayPreferenceService() {
		super(PayPreference.class);
	}

	@Resource
	private IPayPreferenceRepository repository;
	@Override
	public PayPreference queryPreference(String code) {
		return repository.queryPreference(code);
	}

	@Override
	protected GenericRepository<PayPreference, Integer> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	
	
	

}
