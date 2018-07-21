package com.hitler.controller.support;


import java.io.Serializable;

public class ResponseData implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 返回数据
	 */
	private Object result;
	
	private String respCode;
	
	private String respMsg;


	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	
}
