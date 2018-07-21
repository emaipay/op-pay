package com.hitler.core.dto;

import java.io.Serializable;

import com.hitler.core.Constants;

public class ResultDTO<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 操作结果,true-成功;false-失败
	 */
//	@ApiDoc(comments = "操作结果,true-成功;false-失败")
	private Boolean success;
	/**
	 * 返回消息
	 */
	private String respMsg;
	
	/**
	 * 返回编码
	 */
	private String respCode;
	/**
	 * 返回数据
	 */
//	@ApiDoc(comments = "返回数据,可能是msg或者数据")
	private T result;
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	
	
	public String getRespMsg() {
		return respMsg;
	}
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public static <T> ResultDTO<T> success(T result,String respCode, String respMsg) {
		ResultDTO<T> r = new ResultDTO<T>();
		r.setSuccess(true);
		r.setRespMsg(respMsg);
		r.setResult(result);
		r.setRespCode(respCode);
		return r;
	}
	
	public static <T> ResultDTO<T> success() {
		return success(null,Constants.SUCCESS, null);
	}
	
	public static <T> ResultDTO<T> success(String respMsg) {
		return success(null,Constants.SUCCESS, respMsg);
	}
	
	public static <T> ResultDTO<T> success(T result,String respMsg) {
		return success(result,Constants.SUCCESS, respMsg);
	}
	
	public static <T> ResultDTO<T> success(T result) {
		return success(result,Constants.SUCCESS, null);
	}
	
	public static <T> ResultDTO<T> error(String respCode, String respMsg) {
		ResultDTO<T> r = new ResultDTO<T>();
		r.setSuccess(false);
		r.setRespMsg(respMsg);
		r.setRespCode(respCode);
		return r;
	}

}
