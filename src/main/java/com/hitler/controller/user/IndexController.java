package com.hitler.controller.user;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hitler.controller.support.GenericController;
import com.hitler.core.realm.ShiroCacheUtil;
import com.hitler.dto.user.NavMenuDTO;
import com.hitler.entity.PayUser.OnlineStatus;
import com.hitler.gen.PayUser_;
import com.hitler.service.IPayPermissionService;
import com.hitler.service.IPayUserService;

/**
 * 
 * @author jt_wangshuiping
 *
 */
@Controller
public class IndexController extends GenericController {

	@Resource
	private IPayPermissionService payPermissionService;
	@Resource
	private IPayUserService payUserService;

	/**
	 * 首页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/back/index")
	public String index(Model model) {
		return "/login/index";
	}

	/**
	 * 欢迎页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/back/welcome")
	public String welcome(Model model) {
		return "/login/welcome";
	}

	/**
	 * 顶部页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/back/top")
	public String top(HttpSession session) {
		return "/common/top";
	}

	/**
	 * 左侧菜单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/back/west")
	public String west(HttpSession session,Model model) {
		Subject subject = SecurityUtils.getSubject();
		
		if (!subject.isAuthenticated()) {
			Integer userId=ShiroCacheUtil.getUserId();
			try {
				payUserService.updateUser(userId, OnlineStatus.离线, PayUser_.onlineStatus.getName());
			}catch(Exception e){
				e.printStackTrace();
			}
			subject.logout();
			return "/login/login";
		}
		List<NavMenuDTO> menuJson = payPermissionService.getMenuPermissionList(ShiroCacheUtil.getUserId());
		model.addAttribute("menus", menuJson);
		return "/common/west";
	}

}
