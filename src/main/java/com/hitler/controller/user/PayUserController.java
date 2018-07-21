package com.hitler.controller.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hitler.controller.support.CRUDController;
import com.hitler.core.exception.EntityNotExistsException;
import com.hitler.core.realm.SaltUtil;
import com.hitler.core.realm.ShiroCacheUtil;
import com.hitler.core.realm.ShiroUser;
import com.hitler.core.repository.DynamicSpecifications;
import com.hitler.core.repository.SearchFilter;
import com.hitler.dto.user.PayUserCreateDTO;
import com.hitler.dto.user.PayUserDTO;
import com.hitler.dto.user.PayUserUpdateDTO;
import com.hitler.entity.PayRole;
import com.hitler.entity.PayUser;
import com.hitler.gen.PayUser_;
import com.hitler.service.IPayRoleService;
import com.hitler.service.IPayUserService;
import com.hitler.service.support.IGenericService;
import com.hitler.table.PayUserTable;

/**
 * 用户管理控制层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 上午11:46:42
 */
@Controller
@RequestMapping("back/" + PayUserController.PATH)
public class PayUserController extends
		CRUDController<PayUser, Integer, PayUserDTO, PayUserCreateDTO, PayUserUpdateDTO, PayUserTable<PayUserDTO>> {

	public static final String PATH = "payUser";

	@Resource
	private IPayUserService payUserService;
	
	@Resource
	private IPayRoleService payRoleService;

	public PayUserController() {
		super(PATH);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IGenericService<PayUser, Integer> getService() {
		// TODO Auto-generated method stub
		return payUserService;
	}

	@Override
	protected void postList(Model model, Pageable pageable, ServletRequest request, List<SearchFilter> filters)
			throws Exception {
	
	}
	
	@Override
	@RequiresPermissions(value="back/payUser")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, ServletRequest request) throws Exception {
		return super.list(model, request);
	}

	@Override
	 @RequiresPermissions(value="payUser/create")
	protected void postCreate(Model model, PayUserCreateDTO createDTO, BindingResult br) throws Exception {
		super.postCreate(model, createDTO, br);
	}
	
	

	@Override
	@RequiresPermissions(value="payUser/update")
	protected void preUpdate(Model model, PayUserUpdateDTO updateDTO) throws Exception {
		// TODO Auto-generated method stub
		super.preUpdate(model, updateDTO);
	}

	/**
	 * 创建管理员账号
	 *
	 * @param model
	 * @param createDTO
	 * @param br
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	@RequestMapping(value = { "/create" }, method = RequestMethod.POST)
	public String create(Model model, PayUserCreateDTO createDTO, BindingResult br, ServletRequest request)
			throws Exception {
		try {
			if (!br.hasErrors()) {
				postCreate(model, createDTO, br);
				try {
					// 加盐
					createDTO.setSalt(SaltUtil.getSalt(createDTO.getUserName(), createDTO.getPwdLogin()));
					// 给管理员 密码加盐
					createDTO.setPwdLogin(SaltUtil.encodeMd5Hash(createDTO.getPwdLogin(), createDTO.getSalt()));
					createDTO.setCreatedDate(new Date());
					payUserService.creatAdmin(createDTO);
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			}
		} catch (Exception e) {
			log.info("管理员新增后台管理用户异常：" + e.getClass() + "&" + e.getMessage());
		}
		return PATH + "/create";
	}


	/**
	 * 是否存在用户
	 *
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = { "/IsExists" }, method = RequestMethod.POST)
	@ResponseBody
	public Boolean IsExists(String userName) {
		return !payUserService.isExistUser(ShiroCacheUtil.getUserTenantCode(), userName);
	}


	/**
	 * 登录锁定/解锁
	 */
	@RequestMapping(value = { "/login-locked:{locked}/{id}" }, method = RequestMethod.GET)
	 @RequiresPermissions(value="payUser/login-locked")
	public String loginLocked(@PathVariable Integer id, @PathVariable Boolean locked) throws Exception {
		try {
			payUserService.updateUser(id, locked, "loginLocked");

		} catch (Exception e) {
			log.info("管理员对前台账户登录锁定/解锁异常：" + e.getClass() + "&" + e.getMessage());
		}
		return PATH + "/list";
	}

	@RequestMapping(value = { "/account-locked:{locked}/{ids}" }, method = RequestMethod.GET)
	 @RequiresPermissions(value="payUser/account-locked")
	public String batchAccountLocked(@PathVariable Integer locked, @PathVariable Integer ids) throws Exception {
		PayUser.AccountStatus as = locked == 2 ? PayUser.AccountStatus.停用
				: locked == 0 ? PayUser.AccountStatus.锁定 : PayUser.AccountStatus.正常;
		try {
			payUserService.updateUser(ids, as, PayUser_.accountLocked.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PATH + "/list";
	}

	/**
	 * 启用和停用管理员账户
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	@RequiresPermissions(value="payUser/update-status")
	@RequestMapping(value = { "/update-status:{status}/{id}" }, method = RequestMethod.GET)
	public String updateAdminByStu(@PathVariable Integer id, @PathVariable Integer status) throws Exception {
		try {
			PayUser user = getEntityByID(id);
			if (user == null) {
				throw EntityNotExistsException.ERROR;
			}
			PayUser.AccountStatus as = status == 0 ? PayUser.AccountStatus.正常
					: status == 2 ? PayUser.AccountStatus.停用 : PayUser.AccountStatus.正常;
			payUserService.updateUser(id, as, PayUser_.accountLocked.getName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PATH + "/list";
	}

	@Override
	@RequestMapping(value = { "/update/{id}" }, method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute PayUserUpdateDTO updateDTO, BindingResult br,Model model) throws Exception {
		try {
			if (!br.hasErrors()) {
				try {
					ShiroUser currentUser = ShiroCacheUtil.getCurShiroUser();
					if (!updateDTO.getUserName().equals(currentUser.getUserName())) {
						if (!currentUser.getUserName().equals("admin")) {
							br.rejectValue("userName", "userName", "非超级管理员没有权限修改其他管理员密码！");
						}
					}
					PayUser user=payUserService.find(updateDTO.getId());
					String pwd=SaltUtil.encodeMd5Hash(updateDTO.getPwdLogin(), user.getPwdSalt());
					payUserService.updateUser(user.getId(), pwd, "pwdLogin");
					model.addAttribute("userName", user.getUserName());
				} catch (Exception e) {
					br.rejectValue("password", "password", e.getMessage());
				}
			}
		} catch (Exception e) {
			log.info("管理员修改后台账号密码异常：" + e.getClass() + "&" + e.getMessage());
		}
		return PATH + "/update";
	}
	
	/**
     * 进入角色分配页面
     */
    //@RequiresPermissions(value = "admin/distributionRole")
	@RequiresPermissions(value="payUser/distributionRole")
    @RequestMapping(value = {"/distributionRole/{id}"}, method = RequestMethod.GET)
    public String distributionRole(Model model, @PathVariable Integer id) throws Exception {
        PayUser user = payUserService.find(id);
        String roleIds = "";
        for (PayRole r : payUserService.getRolesByUserId(id)) {
            roleIds += r.getId() + ",";
        }
        roleIds = roleIds.equals("") ? "" : roleIds.substring(0, roleIds.length() - 1);

        model.addAttribute("user", user);
        model.addAttribute("roleIds", roleIds);
        Collection<SearchFilter> filters = new ArrayList<SearchFilter>();
        // 角色类型1-前台，2-后台
        model.addAttribute("roleList", payRoleService.findAll(DynamicSpecifications.bySearchFilter(filters)));
        return PATH + "/distributionRole";
    }
    
    /**
     * 批量角色分配保存处理
     */
    //@RequiresPermissions(value = "admin/batchDistributionRole")
    @RequestMapping(value = {"/batchDistributionRoleSave"}, method = RequestMethod.POST)
    public String batchDistributionRoleSave(String userIds, Integer[] roleIds) throws Exception {
        if (userIds == null) {
            new Exception("userId不能为空！");
        }
        try {
            payRoleService.distributionRoleSave(userIds,roleIds);
        } catch (Exception e) {
            throw e;
        }
        return "redirect:/back/" + PayUserController.PATH;
    }
    
    
    /**
     * 选择指定用户
     */
    @RequestMapping(value = {"/select-users"}, method = RequestMethod.GET)
    public String selectParent(Model model) throws Exception {
        return PATH + "/select-user";
    }

    
    /**
     * 修改用户密码页面跳转
     *
     * @return
     */
    @RequestMapping(value={"/update-password"},method=RequestMethod.GET)
    public String skipPwLogin() {
        return PATH + "/update-password";
    }
    
    @RequestMapping(value={"/update-pwLogin"},method=RequestMethod.POST)
    public String updatePwd(@RequestParam String oldPwdLogin, @RequestParam String pwdLogin) throws Exception{
    	try {
            ShiroUser curShiroUser = ShiroCacheUtil.getCurShiroUser();
            String pwdSalt = payUserService.find(curShiroUser.getUserId()).getPwdSalt();
            String oldPassword = SaltUtil.encodeMd5Hash(oldPwdLogin, pwdSalt);
            String newPassword = SaltUtil.encodeMd5Hash(pwdLogin, pwdSalt);
            payUserService.updatePwLogin(curShiroUser.getUserId(), oldPassword, newPassword);

        } catch (Exception e) {
            throw e;
        }
        return PATH + "/list";
    }

}
