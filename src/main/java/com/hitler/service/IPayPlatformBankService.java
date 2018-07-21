package com.hitler.service;

import java.util.List;

import com.hitler.entity.PayPlatformBank;
import com.hitler.service.support.IGenericService;

/**
 * 第三方支付平台对应支付方式配置 service层
 * @author klp
 * @version 创建时间：2017年5月3日 下午2:06:14 
 */
public interface IPayPlatformBankService extends IGenericService<PayPlatformBank, Integer> {

	/**
	 * 批量删除
	 * @param idList
	 */
	void deleteByIdList(List<Integer> idList);
	
	/**
	 * 根据支付类型id查询对应银行 
	 * @param payPlatformId
	 * @return
	 */
	public List<PayPlatformBank> queryByPlatformId(Integer payPlatformId);
 

}
