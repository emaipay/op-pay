package com.hitler.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayMerchant;
import com.hitler.repository.IPayMerchantRepository;
import com.hitler.service.IPayMerchantService;
import com.hitler.service.support.GenericService;

/**
 * 第三方商户
 * @author klp
 *
 */
@Service
public class PayMerchantService extends GenericService<PayMerchant, Integer> implements IPayMerchantService {
	
	@Resource
	private IPayMerchantRepository repository;

	public PayMerchantService() {
		super(PayMerchant.class);
	}

	@Override
	protected GenericRepository<PayMerchant, Integer> getRepository() {
		return repository;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void updateStatus(Integer ids, Object value, String cloumName)
			throws Exception {
		// 这个cloumName在controller传入，不在页面传
		String hql = "update PayMerchant set " + cloumName + "= :value where id=:ids ";
		Map<String,Object> parms = Maps.newHashMap();
		parms.put("value", value);
		parms.put("ids", ids);
		repository.updateByHQL(hql, parms);
	}
}
