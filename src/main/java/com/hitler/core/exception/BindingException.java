package com.hitler.core.exception;


import org.springframework.validation.BindingResult;
/**
 * 绑定异常,用于表单数据验证
 * 注：绑定异常国际化的code在相应的验证器里做
 * @author yang
 */
public class BindingException extends Exception {

	private static final long serialVersionUID = 5654145024170831545L;
	
	private String code;
	
	private BindingResult br;
	
	public BindingException(BindingResult br){
		this.br=br;
	}
	
	public BindingException(String code) {
		this.code = code;
	}
	
/*	public BindingException(String code, String msg) {
		super(msg);
		this.code = code;
	}*/

	public String getCode() {
		return code;
	}
	
	public BindingResult getBr() {
		return br;
	}

	@Override
    public final String toString() {
        String msg = getCode();
        if (getMessage() != null && getMessage().trim().length() > 0)
            msg = ":" + getMessage();
        return msg;
    }

}
