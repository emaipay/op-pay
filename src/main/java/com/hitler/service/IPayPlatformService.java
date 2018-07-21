package com.hitler.service;

import java.util.List;

import com.hitler.entity.PayPlatform;
import com.hitler.entity.PayPlatformBank;
import com.hitler.service.support.IGenericService;

/**
 * 支付平台服务 service层
 * 
 * @author jt-x 2016-03-02
 */
public interface IPayPlatformService extends IGenericService<PayPlatform, Integer> {

	/**
	 * 查找支付平台对应银行的功能id
	 */
	public PayPlatformBank queryPayPlatformBankCode(Integer bankId, Integer platformId);

	
	/**
	 * 批量删除用户
	 * @param ids
	 */
	public void deleteByIdList(List<Integer> ids);

	
	 
}
