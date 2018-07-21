package com.hitler.service;


import java.util.List;

import com.hitler.entity.PayPlatformType;
import com.hitler.service.support.IGenericService;

/**
 * 支付类型Service层
 * 
 * @author klp
 *
 */

public interface IPayPlatformTypeService extends IGenericService<PayPlatformType, Integer> {

	
	/**
	 * 批量删除用户
	 * @param ids
	 */
	public void deleteByIdList(List<Integer> ids); 
	
	
}
