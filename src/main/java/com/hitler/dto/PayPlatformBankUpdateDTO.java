package com.hitler.dto;

 
 import java.util.Date;

import com.hitler.core.dto.TransientDTO;
import com.hitler.entity.PayBank;
import com.hitler.entity.PayPlatform;


 /**
  * 第三方支付平台对应支付方式配置DTO
  * @author klp
  *
  */
public class PayPlatformBankUpdateDTO extends TransientDTO<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4184082507038940965L;

	private  Integer id;

	private String createdBy;

	private Date createdDate;

	private String lastModifiedBy;

	private Date lastModifiedDate;

	private String payCode;

	private  PayBank bankId;

	private  PayPlatform payPlatformId;

	public void setId( Integer id){
	this.id=id;
	}
	public  Integer getId(){
		return id;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
 
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
 
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public PayBank getBankId() {
		return bankId;
	}
	public void setBankId(PayBank bankId) {
		this.bankId = bankId;
	}
	public PayPlatform getPayPlatformId() {
		return payPlatformId;
	}
	public void setPayPlatformId(PayPlatform payPlatformId) {
		this.payPlatformId = payPlatformId;
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
	
	
}

