package com.hitler.service;

import java.util.List;

import com.hitler.entity.PayConfig;
import com.hitler.entity.PayConfig.MethodType;
import com.hitler.service.support.IGenericService;

/**
 * 各支付平台配置Service层
 * @author klp
 *
 */
public interface IPayConfigService extends IGenericService<PayConfig, Integer> {
	
	/**
	 * 查询支付配置
	 * @param type
	 * @param methodType
	 * @return
	 */
	public PayConfig queryConfigByType(String type,MethodType methodType);

	/**
	 * 批量删除用户
	 * @param ids
	 */
	public void deleteByIdList(List<Integer> ids); 
	
	
}
