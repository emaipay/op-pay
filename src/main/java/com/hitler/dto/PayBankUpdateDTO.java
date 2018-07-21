package com.hitler.dto;

import java.util.Date;

import com.hitler.core.dto.TransientDTO;

/**
 * 支付平台可支付方式(银行卡)DTO
 * @author klp
 *
 */
public class PayBankUpdateDTO extends TransientDTO<Integer> {

 
	/**
	 * 
	 */
	private static final long serialVersionUID = 5710706635316854226L;

	private Integer id;

	private String createdBy;

	private Date createdDate;

	private String last_modifiedBy;

	private Date last_modifiedDate;

	/**
	 * 是否允许绑定
	 */
	private Integer allowBindingStatus;

	/**
	 * 线下充值状态
	 */
	private Integer bankTransferStatus;

	/**
	 * 银行Logo图片路径
	 */
	private String logoFilePath;

	/**
	 * 银行名称
	 */
	private String name;

	/**
	 * 银行简称
	 */
	private String shortName;

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

 

	public String getLast_modifiedBy() {
		return last_modifiedBy;
	}

	public void setLast_modifiedBy(String last_modifiedBy) {
		this.last_modifiedBy = last_modifiedBy;
	}

 

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLast_modifiedDate() {
		return last_modifiedDate;
	}

	public void setLast_modifiedDate(Date last_modifiedDate) {
		this.last_modifiedDate = last_modifiedDate;
	}

	public Integer getAllowBindingStatus() {
		return allowBindingStatus;
	}

	public void setAllowBindingStatus(Integer allowBindingStatus) {
		this.allowBindingStatus = allowBindingStatus;
	}

	public Integer getBankTransferStatus() {
		return bankTransferStatus;
	}

	public void setBankTransferStatus(Integer bankTransferStatus) {
		this.bankTransferStatus = bankTransferStatus;
	}

	public String getLogoFilePath() {
		return logoFilePath;
	}

	public void setLogoFilePath(String logoFilePath) {
		this.logoFilePath = logoFilePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
