package com.hitler.controller.user;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hitler.controller.support.CRUDController;
import com.hitler.dto.user.PayRoleUserCreateDTO;
import com.hitler.dto.user.PayRoleUserDTO;
import com.hitler.dto.user.PayRoleUserUpdateDTO;
import com.hitler.entity.PayRoleUser;
import com.hitler.service.IPayRoleUserService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayRoleUserTable;

/**
 * 权限分配控制层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 上午11:46:42
 */
@Controller
@RequestMapping("pay/" + PayRoleUserController.PATH)
public class PayRoleUserController extends
		CRUDController<PayRoleUser, Integer, PayRoleUserDTO, PayRoleUserCreateDTO, PayRoleUserUpdateDTO, PayRoleUserTable<PayRoleUserDTO>> {

	public static final String PATH = "payRoleUser";

	@Resource
	private IPayRoleUserService payRoleUserService;

	public PayRoleUserController() {
		super(PATH);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IGenericService<PayRoleUser, Integer> getService() {
		// TODO Auto-generated method stub
		return payRoleUserService;
	}

}
