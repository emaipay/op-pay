package com.hitler.controller.user;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hitler.controller.support.CRUDController;
import com.hitler.dto.user.PayRolePermissionCreateDTO;
import com.hitler.dto.user.PayRolePermissionDTO;
import com.hitler.dto.user.PayRolePermissionUpdateDTO;
import com.hitler.entity.PayRolePermission;
import com.hitler.service.IPayRolePermissionService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayRolePermissionTable;

/**
 * 权限资源管理控制层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 上午11:46:42
 */
@Controller
@RequestMapping("back/" + PayRolePermissionController.PATH)
public class PayRolePermissionController extends
		CRUDController<PayRolePermission, Integer, PayRolePermissionDTO, PayRolePermissionCreateDTO, PayRolePermissionUpdateDTO, PayRolePermissionTable<PayRolePermissionDTO>> {

	public static final String PATH = "payRolePermission";

	@Resource
	private IPayRolePermissionService payRolePermissionService;

	public PayRolePermissionController() {
		super(PATH);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IGenericService<PayRolePermission, Integer> getService() {
		// TODO Auto-generated method stub
		return payRolePermissionService;
	}

}
