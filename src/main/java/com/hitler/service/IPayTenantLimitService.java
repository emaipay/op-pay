package com.hitler.service;

import java.util.List;

import com.hitler.entity.PayTenantLimit;
import com.hitler.service.support.IGenericService;

/**
 *接入商户第三方支付限额 service层
 * @author klp
 *
 */
public interface IPayTenantLimitService extends IGenericService<PayTenantLimit, Integer> {
	
	public PayTenantLimit queryPayConnLimt(Integer connId,Integer platformId);
	
	public List<PayTenantLimit> queryConnLimitByTenantId(Integer tenantId);
	
	public void deleteByTenantId(Integer tenantId);


}
