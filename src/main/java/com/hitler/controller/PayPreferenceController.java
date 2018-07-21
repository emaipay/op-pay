package com.hitler.controller;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hitler.controller.support.CRUDController;
import com.hitler.dto.PayPreferenceCreateDTO;
import com.hitler.dto.PayPreferenceDTO;
import com.hitler.dto.PayPreferenceUpdateDTO;
import com.hitler.entity.PayPreference;
import com.hitler.service.IPayPreferenceService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayPreferenceTable;

/**
 * 配置各个支付平台(常量)控制层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 上午11:46:42
 */
@Controller
@RequestMapping("back/" + PayPreferenceController.PATH)
public class PayPreferenceController extends
		CRUDController<PayPreference, Integer, PayPreferenceDTO, PayPreferenceCreateDTO, PayPreferenceUpdateDTO, PayPreferenceTable<PayPreferenceDTO>> {

	public static final String PATH = "payPreference";

	@Resource
	private IPayPreferenceService payPreferenceService;

	public PayPreferenceController() {
		super(PATH);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	@RequiresPermissions(value="back/payPreference")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, ServletRequest request) throws Exception {
		return super.list(model, request);
	}

	@Override
	protected IGenericService<PayPreference, Integer> getService() {
		// TODO Auto-generated method stub
		return payPreferenceService;
	}

	@Override
	@RequiresPermissions(value="payPreference/create")
	protected void preCreate(Model model, PayPreferenceCreateDTO createDTO, ServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		super.preCreate(model, createDTO, request);
	}

	@Override
	@RequiresPermissions(value="payPreference/create")
	protected void preUpdate(Model model, PayPreferenceUpdateDTO updateDTO) throws Exception {
		// TODO Auto-generated method stub
		super.preUpdate(model, updateDTO);
	}
	
	

}
