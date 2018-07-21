package com.hitler.service;

import java.util.Collection;
import java.util.List;

import com.hitler.dto.PayTenantCreateDTO;
import com.hitler.dto.PayTenantUpdateDTO;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayPlatformBank;
import com.hitler.entity.PayTenant;
import com.hitler.service.support.IGenericService;

/**
 * 接入商户service层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 下午2:26:26
 */
public interface IPayTenantService extends IGenericService<PayTenant, Integer> {

	/**
	 * 批量删除
	 * 
	 * @param idList
	 */
	void deleteByIdList(List<Integer> idList);

	public void updateStatus(Integer ids, Object value, String cloumName) throws Exception;

	public void tenantSave(PayTenantCreateDTO dto) throws Exception;

	public void tenantUpdate(PayTenantUpdateDTO dto) throws Exception;

	public Collection<PayMerchant> getPayMerchantsByTenantId(Integer tenantId);

	public Collection<PayPlatformBank> findByTenantId(Integer tenantId);
	
	public List<PayTenant> queryTenantByUserId(Integer userId);
	
	public PayTenant queryPayTenant(String memberId, String terminalId);
}
