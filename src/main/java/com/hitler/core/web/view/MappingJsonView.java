package com.hitler.core.web.view;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.hitler.controller.support.ResponseData;
import com.hitler.core.exception.BindingException;
import com.hitler.core.exception.BusinessException;
import com.hitler.core.exception.WebBusinessException;
import com.hitler.core.web.resolver.MessageResolver;

/**
 * 返回前端的所有json数据:
 * msg：1全局异常提示：包括业务异常、表单绑定异常、未捕获的异常
 * @author 
 *
 */
public class MappingJsonView {
	private  static final Logger log = LoggerFactory.getLogger(MappingJsonView.class); 
	
	public static final String SUCCESS = "success";
	public static final String MSG = "msg";
	public static final String RESULT = "result";
	
	/**
	 * 失败时返回消息提示:(只返回消息提示)
	 * 后期统一在此处做消息国际化
	 * @param object
	 * @return
	 */
	public static ResponseData msg(String uri,Object object) {
		Map<String, Object> result = new HashMap<String, Object>();
		ResponseData rd = new ResponseData();
		
		if (object instanceof Exception) {
			if (object instanceof BusinessException) {
				String code = ((BusinessException) object).getCode();
				result.put(MSG, (code != null ? MessageResolver.getMessage(code) : ((WebBusinessException) object).getMsg()));
				
				log.info("返回系统业务异常:{},{},{}", rd.getResult(), uri);
			} else if (object instanceof ShiroException) {// shiro异常未做国际化
				if (object instanceof AuthorizationException) {
					result.put(MSG, "抱歉,您无此权限！");
				} else if (object instanceof AuthenticationException) {
					result.put(MSG, ((AuthenticationException) object).getMessage());
				} else {
					result.put(MSG, ((ShiroException) object).getMessage());
				}
				
				log.info("返回权限异常提示:{},{},{}", rd.getResult(), uri);
			} else if (object instanceof BindingException) {
				BindingResult br = ((BindingException) object).getBr();
				if (br.hasFieldErrors()) {
					FieldError error = br.getFieldErrors().get(0);// 返回遇到的第一个错误
					String errorMsg = error.getDefaultMessage() != null ? error.getDefaultMessage()
							: MessageResolver.getMessage(error.getCode());
					result.put(MSG, errorMsg);
					
					log.info("返回表单数据绑定异常,字段：{},异常消息：{}, uri:{}, user:{}", error.getField(), errorMsg, uri);
				}
			} else {
				result.put(MSG, MessageResolver.getMessage("com.hitler.common.exception.ServerInternalException"));
				Exception e = (Exception) object;
				log.error("系统异常,{},{}", uri, e);
			}
		}
		
		rd.setResult(result);
		
		return rd;
	}
	public static ResponseData msg(String uri,String respCode,String respMsg) {
		log.error("[接口调用]调用请求失败:code:{},msg:{},uri:{}",respCode,respMsg,uri);
		ResponseData rd = new ResponseData();
		rd.setRespCode(respCode);
		rd.setRespMsg(respMsg);
		return rd;
	}
   

	public static ResponseData msgOrData(Boolean success,Object object) {
		ResponseData rd=new ResponseData();
		rd.setRespCode("0");
		rd.setRespMsg("SUCCESS");
		if(object instanceof String){
			Map<String, Object> result = new HashMap<String, Object>();
			result.put(MSG, object);
			rd.setResult(result);
		}else{
			rd.setResult(object);
		}
		
		return rd;
	}
}
