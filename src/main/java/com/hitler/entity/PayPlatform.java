package com.hitler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hitler.core.entity.CheckableEntity;


/**
 * @author 第三方支付平台
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_pay_platform")
public class PayPlatform extends CheckableEntity<Integer> {

 

	/**
	 * 
	 */
	private static final long serialVersionUID = -3935840278062557720L;

	@Column(name = "NAME", columnDefinition = "varchar(10) COMMENT '支付平台名称'", nullable = false)
	private String name;

	@Column(name = "pay_url", columnDefinition = "varchar(500) COMMENT '支付平台Url'", nullable = false)
	private String payUrl;

	@Column(name = "platform_code", columnDefinition = "varchar(15) COMMENT '支付类型code'", nullable = false)
	private String platformCode;

	@Column(name = "platform_logo", columnDefinition = "varchar(50) comment '支付类型logo图片'",nullable = false)
	private String platformLogo;
	
	@ManyToOne //@ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, optional = true)
	@JoinColumn(name = "platform_type_id",referencedColumnName="id", columnDefinition = "varchar(10) comment '支付类型'",nullable = false)
	private PayPlatformType platformTypeId;
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getPlatformLogo() {
		return platformLogo;
	}

	public void setPlatformLogo(String platformLogo) {
		this.platformLogo = platformLogo;
	}

	public PayPlatformType getPlatformTypeId() {
		return platformTypeId;
	}

	public void setPlatformTypeId(PayPlatformType platformTypeId) {
		this.platformTypeId = platformTypeId;
	}

	


	
}
