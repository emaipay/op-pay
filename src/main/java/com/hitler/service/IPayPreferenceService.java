package com.hitler.service;

import com.hitler.entity.PayPreference;
import com.hitler.service.support.IGenericService;

/**
 * 用户权限 service 层
 * @author klp
 *
 */
public interface IPayPreferenceService extends IGenericService<PayPreference, Integer>{
	
	public PayPreference queryPreference(String code);

}
