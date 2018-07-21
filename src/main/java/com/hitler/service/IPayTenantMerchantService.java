package com.hitler.service;

import java.util.List;

import com.hitler.entity.PayTenantMerchant;
import com.hitler.service.support.IGenericService;

/**
 * 接入商户对应第三方支付商户Service层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 下午3:11:02
 */
public interface IPayTenantMerchantService extends IGenericService<PayTenantMerchant, Integer> {
	public List<PayTenantMerchant> queryPayMerchant(String memberId,String terminalId);
	
	public List<PayTenantMerchant> findByTenantId(Integer tenantId);
	
	public void deleteByTenantId(Integer tenantId);
}
