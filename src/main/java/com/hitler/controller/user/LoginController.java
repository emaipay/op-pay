package com.hitler.controller.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hitler.controller.support.GenericController;
import com.hitler.core.Constants;
import com.hitler.core.dto.ResultDTO;
import com.hitler.core.dto.login.AdminInfoDTO;
import com.hitler.core.dto.login.UserLoginDTO;
import com.hitler.core.realm.ShiroCacheUtil;
import com.hitler.core.realm.UserToken;
import com.hitler.core.utils.ClientUtil;
import com.hitler.core.utils.ValidUtil;
import com.hitler.entity.PayUser.OnlineStatus;
import com.hitler.gen.PayUser_;
import com.hitler.service.IPayUserService;

/**
 * 后台管理登录控制器
 * @author onsoul by JT 2015-5-30 上午10:14:44
 */
@Controller
public class LoginController extends GenericController {
	protected static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Resource
	private IPayUserService payUserService;
	// 后台管理登录页
	@RequestMapping(value = { "/", "/login" })
	public String backLoginPage(HttpSession session) {
		return "/login/login";
	}

	// 后台登录表单
	@RequestMapping(value = "/backLogin")
	@ResponseBody
	public ResultDTO<?> backLogin(HttpServletRequest req,@Valid UserLoginDTO ul,BindingResult br){
		/*String vcode = (String) req.getSession().getAttribute(Constants.VCODE_PARAM);
		if (null==vcode) {
			return ResultDTO.error(Constants.EXCEPTION,"验证码已失效");
		} 
		if (!vcode.equalsIgnoreCase(ul.getvCode())) {
			return ResultDTO.error(Constants.EXCEPTION,"验证码错误");
		}*/
		AdminInfoDTO adminInfoDTO=null;
		if(br.hasErrors()){
			return ResultDTO.error(Constants.FORM_ERROR,ValidUtil.bindMsgOne(br));
		}
		String IP = ClientUtil.getRemoteAddr(req);// ip
		String userAgent = ClientUtil.getBrowser(req);// 浏览器
		String SID = ClientUtil.getSessionId(req);
		UserToken token = new UserToken(ul.getUserName(), ul.getPassword(),false, userAgent, IP, SID);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			if (subject.isAuthenticated()) {
				adminInfoDTO=new AdminInfoDTO();
				adminInfoDTO.setUserName(ShiroCacheUtil.getUserName());
			}
			ClientUtil.removeVCode(req);
		} catch (Exception e) {
			if(e instanceof AuthenticationException){
				return ResultDTO.error(Constants.EXCEPTION,e.getMessage());
			}else{
				log.error("登录未知异常:",e.getMessage());
				return ResultDTO.error(Constants.EXCEPTION,"网络异常!");
			}
		}
		return  ResultDTO.success(adminInfoDTO);
	}
	

	/**
	 * 后端用户退出
	 */
	@RequestMapping(value = "/bLoginOut")
	public void adminLogout(HttpServletResponse response, HttpServletRequest request) {
		logger.info("### 管理员退出 ...");
		Subject subject = SecurityUtils.getSubject();
		if(null != subject && subject.isAuthenticated()){
			Integer userId=ShiroCacheUtil.getUserId();
			try {
				payUserService.updateUser(userId, OnlineStatus.离线, PayUser_.onlineStatus.getName());
				subject.logout();
				response.sendRedirect("/login");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	

}
