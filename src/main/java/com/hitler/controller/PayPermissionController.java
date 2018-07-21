package com.hitler.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hitler.controller.support.CRUDController;
import com.hitler.dto.user.PayPermissionCreateDTO;
import com.hitler.dto.user.PayPermissionDTO;
import com.hitler.dto.user.PayPermissionTreeDTO;
import com.hitler.dto.user.PayPermissionUpdateDTO;
import com.hitler.entity.PayPermission;
import com.hitler.entity.PayRole;
import com.hitler.service.IPayPermissionService;
import com.hitler.service.IPayRoleService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayPermissionTable;

/**
 * 配置各个支付平台(常量)控制层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 上午11:46:42
 */
@Controller
@RequestMapping("back/" + PayPermissionController.PATH)
public class PayPermissionController extends
		CRUDController<PayPermission, Integer, PayPermissionDTO, PayPermissionCreateDTO,PayPermissionUpdateDTO, PayPermissionTable<PayPermissionDTO>> {

	public static final String PATH = "payPermission";

	@Resource
	private IPayPermissionService payPermissionService;
	@Resource
	private IPayRoleService payRoleService;

	public PayPermissionController() {
		super(PATH);
		// TODO Auto-generated constructor stub
	}

	@Override 
	protected IGenericService<PayPermission, Integer> getService() {
		// TODO Auto-generated method stub
		return payPermissionService;
	}
	
	/**
     * 表单页面 - 新增权限
     * @param model
     * @param createDTO
     * @param request
     * @throws Exception
     */
    @Override
    protected void preCreate(Model model, PayPermissionCreateDTO createDTO,
                             ServletRequest request) throws Exception {
        String parentPermissionId = request.getParameter("parentPermissionId");
        String floor = request.getParameter("floor");
        createDTO.setParentPermissionId(Integer.parseInt(parentPermissionId));
        createDTO.setFloor(Integer.parseInt(floor));
    }
    @Override
    protected void postCreate(Model model, PayPermissionCreateDTO createDTO, BindingResult br) throws Exception {
        super.postCreate(model, createDTO, br);
    }
    
    
    
    @Override
	protected void preUpdate(Model model, PayPermissionUpdateDTO updateDTO) throws Exception {
		// TODO Auto-generated method stub
		super.preUpdate(model, updateDTO);
	}

//	@Override
//    public String update(@Valid @ModelAttribute PayPermissionUpdateDTO updateDTO, BindingResult br,Model model) throws Exception {
//        if (!br.hasErrors()) {
//            getService().update(updateDTO);
//        }
//        return PATH + "/update";
//    }

  /*  @Override
	@RequiresPermissions(value="back/payPermission")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, ServletRequest request) throws Exception {
		return super.list(model, request);
	}*/
    
	 /**
     * 获取permission列表json给（后台）
     */
    @RequestMapping(value = "/json",method = RequestMethod.GET)
    @ResponseBody
    public List<PayPermissionTreeDTO> json() {
        List<PayPermissionTreeDTO> list = payPermissionService.permissionTree();
        return list;
    }
    
    /**
     * 权限树弹出框：供后台角色分配
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/tree/{id}", method = RequestMethod.GET)
    public String tree(Model model, @PathVariable Integer id, ServletRequest request) throws Exception {
        PayRole role = payRoleService.find(id);
        String permissionIds = "";
        for(PayPermissionDTO p : payPermissionService.findByRoleId(id)) {
            permissionIds += p.getId() + ",";
        }
        permissionIds = permissionIds.equals("") ? "" : permissionIds.substring(0, permissionIds.length()-1);

        model.addAttribute("role", role);
        model.addAttribute("permissionIds", permissionIds);
        return PATH + "/tree";
    }
    
    /**
     * 判断权限名是否存在
     * @param code
     * @return
     */
    @RequestMapping("/existsCode")
    @ResponseBody
    public Boolean existsCode(String code){
        return payPermissionService.findByCode(code) == null;
    }
    
    /**
     * 权限分配保存
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    public String batch(Integer roleId, String permissions,ServletRequest request) throws Exception {
        payPermissionService.permissionSave(roleId,permissions);
        return PATH + "/list";
    }


}
