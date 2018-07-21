package com.hitler.core.enums;

/**
 * 支付类型表对应枚举
 * 必须与t_pay_platform_type表platform_type对应
 * @author xu
 * 2017-04-27
 */
public enum PlatformTypeEnum {
	易付宝("YIFUBAO"), 
	摩宝("MOBAO"), 
	秒付("MIAOFU"),
	闪付("SHANFU"), 
	金付卡("JINFUKA"), 
	融合("RONGHE"),
	乐付("LEFU"),
	云聚付("YUNJUFU"),
	支付宝("ALIPAY"),
	微信("WEIXIN"),
	模酷支付("KMZF"),
	
	;
	private String name;

	PlatformTypeEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
