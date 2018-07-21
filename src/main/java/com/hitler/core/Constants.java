package com.hitler.core;

/**
 * 常量类
 * @author xu
 *
 */
public class Constants {
	
	//支付相关
	public static final String SUCCESS="0000";//成功
	public static final String MERCHANT_NO_EMPTY="1000";//商户号不能为空
	public static final String TERMINAL_NO_EMPTY="1001";//终端号不能为空
	public static final String MERCHANT_NOT_EXISTS="1002";//商户不存在
	public static final String MER_KEY_ERROR="1003";//接入平台秘钥错误
	public static final String PARAM_EMPTY="1004";//参数缺失
	public static final String MONEY_ERROR="1005";//金额错误
	public static final String SIGN_ERROR="1006";//签名错误
	public static final String ORDER_SAVE_ERROR="1007";//订单保存错误
	public static final String REFLECT_ERROR="1008";//查找支付类反射错误
	public static final String ORDER_EMPTY="1009";//订单查询为空
	public static final String PAY_BACK_PAGE_ERROR="1010";//回调页面失败
	public static final String PAY_FAILURE="1011";//回调页面失败
	public static final String CLASS_REFLECT_ERROR="1012";//类反射失败
	public static final String ORDER_NO_EMPTY="1013";//订单号为空
	public static final String ORDER_NO_REPEAT="1014";//订单号重复
	
	//订单查询code
	public static final String PAY_SUCCESS="1";//支付成功
	public static final String NO_ORDER="4";//未查询到该订单
	public static final String NO_PAY_ORDER="0";//订单未付款
	public static final String PAY_FAIL="2";//订单支付失败
	//登录
	public static final String VCODE = "vCode";//验证码code
	public static final String FORM_ERROR="2000";//绑定表单错误
	
	
	//异常
	public static final String EXCEPTION="ERROR";
	
	//验证码标识
	public static final String VCODE_PARAM="vCode";
	/**
	 * 代付失败
	 */
	public static final String DRAW_ERROR="2001";
	
	

}
