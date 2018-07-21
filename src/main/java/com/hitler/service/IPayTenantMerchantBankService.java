package com.hitler.service;

import java.util.List;

import com.hitler.entity.PayTenantMerchantBank;
import com.hitler.service.support.IGenericService;

/**
 * 接入商户对应商户的支付方式(银行卡)  Service层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 下午3:11:02
 */
public interface IPayTenantMerchantBankService extends IGenericService<PayTenantMerchantBank, Integer> {
	public List<PayTenantMerchantBank> queryPayConnMerchantBank(Integer merchantId,Integer connId);

	/**
	 * 批量删除
	 * @param idList
	 */
	public void deleteByIdList(List<Integer> idList);
	
	public List<PayTenantMerchantBank> findByTenantId(Integer tenantId);
	
	public void deleteByTenantIdId(Integer tenantId)  throws Exception ;
	
	List<Integer> queryPayBankIdList(Integer connId,Integer merchantId);

}
