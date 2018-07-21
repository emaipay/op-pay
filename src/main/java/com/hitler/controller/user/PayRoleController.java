package com.hitler.controller.user;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hitler.controller.support.CRUDController;
import com.hitler.core.dto.ResultDTO;
import com.hitler.core.repository.SearchFilter;
import com.hitler.dto.user.PayRoleCreateDTO;
import com.hitler.dto.user.PayRoleDTO;
import com.hitler.dto.user.PayRoleUpdateDTO;
import com.hitler.entity.PayRole;
import com.hitler.entity.PayRoleUser;
import com.hitler.service.IPayPermissionService;
import com.hitler.service.IPayRoleService;
import com.hitler.service.IPayRoleUserService;
import com.hitler.service.IPayUserService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayRoleTable;

/**
 * 角色管理控制层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 上午11:46:42
 */
@Controller
@RequestMapping("back/" + PayRoleController.PATH)
public class PayRoleController extends
		CRUDController<PayRole, Integer, PayRoleDTO, PayRoleCreateDTO, PayRoleUpdateDTO, PayRoleTable<PayRoleDTO>> {

	public static final String PATH = "payRole";

	@Resource
	private IPayRoleService payRoleService;
	
	@Resource
	private IPayRoleUserService payRoleUserService;
	
	@Resource
	private IPayPermissionService payPermissionService;
	@Resource
	private IPayUserService payUserService;

	public PayRoleController() {
		super(PATH);
	}

	@Override
	protected IGenericService<PayRole, Integer> getService() {
		return payRoleService;
	}

	@Override
	protected void postList(Model model, Pageable pageable, ServletRequest request, List<SearchFilter> filters)
			throws Exception {
		// 会员角色
	}

	@Override
	@RequiresPermissions(value="back/payRole")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, ServletRequest request) throws Exception {
		return super.list(model, request);
	}

	@Override
	@RequiresPermissions(value="payRole/create")
	@RequestMapping(value = { "/create" }, method = RequestMethod.GET)
	public String create(Model model, ServletRequest request) throws Exception {
		return super.create(model, request);
	}

	@Override
	@RequiresPermissions(value="payRole/update")
	@RequestMapping(value = { "/update/{id}" }, method = RequestMethod.GET)
	public String update(Model model, @PathVariable Integer id) throws Exception {
		return super.update(model, id);
	}

	@Override
	@RequiresPermissions(value="payRole/delete")
	@RequestMapping(value = { "/delete/{id}" }, method = RequestMethod.GET)
	public String delete(@PathVariable Integer id) throws Exception {
		return super.delete(id);
	}
	
	@RequestMapping(value = { "/ajaxdel/{id}" }, method = RequestMethod.GET)
    @ResponseBody
    @RequiresPermissions(value="payRole/delete")
    public ResultDTO<?> ajaxDel(@PathVariable Integer id) throws Exception {
        List<PayRoleUser> roleUsers = payRoleUserService.findByRoleId(id);
        if(null != roleUsers && roleUsers.size() > 0) {
            return ResultDTO.error("-1","该角色已关联用户信息，<br>请先清除关系后重试！");
        }

        try {
            PayRole role = payRoleService.find(id);
            if(null != role){
            	payPermissionService.deleteByRoleId(id);
            }
        } catch (Exception e) {
            return ResultDTO.error("-1","删除错误：" + e.getMessage());
        }

        return ResultDTO.success();
    }
	
//	/**
//     * 进入角色分配页面
//     */
//	@RequiresPermissions(value="payRole/distributionRole")
//    @RequestMapping(value = { "/distributionRole/{id}" }, method = RequestMethod.GET)
//    public String distributionRole(Model model, @PathVariable Integer id)
//            throws Exception {
//        PayUser user = payUserService.find(id);
//        String roleIds = "";
//        for (PayRoleDTO r : payRoleService.getRolesByUserId(id)) {
//            roleIds += r.getId() + ",";
//        }
//        roleIds = roleIds.equals("") ? "" : roleIds.substring(0, roleIds.length() - 1);
//
//        model.addAttribute("user", user);
//        model.addAttribute("roleIds", roleIds);
//        // 角色类型1-前台，2-后台
//        model.addAttribute("roleList", payRoleService.findAll());
//        return PATH + "/distributionRole";
//    }
  

   
}
