package com.hitler.service;

import java.util.List;

import com.hitler.entity.PayBank;
import com.hitler.service.support.IGenericService;

/**
 * 支付平台可支付方式(银行卡)Service层
 * 
 * @author klp
 *
 */
public interface IPayBankService extends IGenericService<PayBank, Integer> {

	/**
	 * 查找支付方式记录
	 * 
	 * @param merchantId
	 * @param connId
	 * @return
	 */
	public List<PayBank> queryPayBank(Integer merchantId, Integer connId);

	/**
	 * 批量删除
	 * @param idList
	 */
	public void deleteByIdList(List<Integer> idList);
	
	/**
	 * 查询支付类型对应的银行
	 * @param payPlatformId
	 * @return
	 */
	public List<PayBank> queryPlatformBank(Integer payPlatformId);
	/**
	 * 查询银行列表
	 * @param ids
	 * @return
	 */
	public List<PayBank> queryPayBankByIdList(List<Integer> ids);	 
	
}
