package com.hitler.dto;

import com.hitler.core.dto.TransientDTO;

/**
 * 支付类型大类Dto
 * @author klp
 *
 */
public class PayPlatformTypeDTO  extends TransientDTO<Integer>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -154442473599764574L;

	private Integer id;

	private String createdBy;

	private String createdDate;

	private String lastModifiedBy;
	 
	private String lastModifieDate;

	private String platformType;

	private String typeName;

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

	public String getLastModifieDate() {
		return lastModifieDate;
	}

	public void setLastModifieDate(String lastModifieDate) {
		this.lastModifieDate = lastModifieDate;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

 
	 
}
