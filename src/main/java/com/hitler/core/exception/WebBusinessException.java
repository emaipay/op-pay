package com.hitler.core.exception;

/**
 * 控制器层的业务异常（后期方便做国际化）
 * 只要找抛出异常的这个类就行
 * @author yang
 *
 */
public class WebBusinessException extends BusinessException {

	private static final long serialVersionUID = 5654145024170831545L;
	
	private String code;
	private String msg;
	
	public WebBusinessException(String msg) {
		super(null);//未做国际化的code为null
		this.msg=msg;
	}
	public WebBusinessException(String code, String msg) {
		super(msg);
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getMsg() {
		return msg;
	}
	
}
