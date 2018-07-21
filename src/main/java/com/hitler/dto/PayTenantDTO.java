package com.hitler.dto;

import com.hitler.core.dto.TransientDTO;
import com.hitler.entity.PayUser;

/**
 * 接入商户DTO
 * 
 * @author klp
 *
 */
public class PayTenantDTO extends TransientDTO<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6266149694787361679L;

	private Integer id;

	private String createdBy;

	private String createdDate;

	private String lastModifiedBy;

	private String lastModifiedDate;

	/**
	 * 接入平台域名
	 */
	private String memberDomain;

	/**
	 * 接入平台分配的平台号
	 */
	private String memberId;

	/**
	 * 接入平台名称
	 */
	private String platformName;

	/**
	 * 接入平台分配的终端号
	 */
	private String terminalId;

	/**
	 * 接入平台分配的秘钥
	 */
	private String merKey;

	private PayUser userId;
	
	private Boolean available;
	
	

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getMemberDomain() {
		return memberDomain;
	}

	public void setMemberDomain(String memberDomain) {
		this.memberDomain = memberDomain;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getMerKey() {
		return merKey;
	}

	public void setMerKey(String merKey) {
		this.merKey = merKey;
	}

	public PayUser getUserId() {
		return userId;
	}

	public void setUserId(PayUser userId) {
		this.userId = userId;
	}



}
