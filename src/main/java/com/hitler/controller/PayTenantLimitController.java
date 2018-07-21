package com.hitler.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hitler.controller.support.CRUDController;
import com.hitler.dto.PayTenantLimitCreateDTO;
import com.hitler.dto.PayTenantLimitDTO;
import com.hitler.dto.PayTenantLimitUpdateDTO;
import com.hitler.entity.PayTenantLimit;
import com.hitler.service.IPayTenantLimitService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayTenantLimitTable;

/**
 * 接入商户第三方支付限额控制层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 上午11:46:42
 */
@Controller
@RequestMapping("pay/" + PayTenantLimitController.PATH)
public class PayTenantLimitController extends
		CRUDController<PayTenantLimit, Integer, PayTenantLimitDTO,PayTenantLimitCreateDTO, PayTenantLimitUpdateDTO, PayTenantLimitTable<PayTenantLimitDTO>> {

	public static final String PATH = "payTenantLimit";

	@Resource
	private IPayTenantLimitService iPayTenantLimitService;

	public PayTenantLimitController() {
		super(PATH);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IGenericService<PayTenantLimit, Integer> getService() {
		// TODO Auto-generated method stub
		return iPayTenantLimitService;
	}

}
