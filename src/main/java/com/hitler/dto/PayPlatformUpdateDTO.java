package com.hitler.dto;

import java.util.Date;

import com.hitler.core.dto.TransientDTO;
import com.hitler.entity.PayPlatformType;

/**
 * 第三方支付平台管理 Dto
 * @author klp
 *
 */
public class PayPlatformUpdateDTO extends TransientDTO<Integer> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8808050111576810806L;


	private Integer id;

	private String createdBy;

	private Date createdDate;

	private String lastModifiedBy;

	private Date lastModifiedDate;

	/**
	 * 支付平台名称
	 */
	private String name;

	/**
	 * 支付平台Url
	 */
	private String payUrl;

	/**
	 * 支付类型code
	 */
	private String platformCode;

	/**
	 * 支付类型logo图片
	 */
	private String platformLogo;
	
	
	
	
	private PayPlatformType platformTypeId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}



	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

 
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
