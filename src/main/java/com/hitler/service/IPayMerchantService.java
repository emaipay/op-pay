package com.hitler.service;

import com.hitler.entity.PayMerchant;
import com.hitler.service.support.IGenericService;

/**
 *第三方商户 Service层
 * @author klp
 *
 */
public interface IPayMerchantService extends IGenericService<PayMerchant, Integer> {
	public void updateStatus(Integer ids, Object value, String cloumName)
			throws Exception;
}
