package com.hitler.payservice.platform.rh;

/**
 * 配置信息
 * 
 * @author nicholas
 *
 */
public class ConfigInfo {

    /** +Lepay服务器地址 **/

    // public static final String server_location = "https://openapi.lechinepay.com/pre.lepay.api";//正式
    public static final String server_location  = "https://lepay.asuscomm.com/pre.lepay.api"; // 自验
    // public static final String server_location = "https://lepay.asuscomm.com:3443/pre.lepay.api"; //测试

    /** 支付短链接 **/
    public static final String short_url_pay    = "/order/add";

    /** 查询短链接 **/
    public static final String short_url_query  = "/order/query";

    /** 退款短链接 **/
    public static final String short_url_refund = "/order/refund";
}
