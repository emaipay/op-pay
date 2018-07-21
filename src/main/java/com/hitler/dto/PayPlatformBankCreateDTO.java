package com.hitler.dto;

 
 import com.hitler.core.dto.TransientDTO;
import com.hitler.entity.PayBank;
import com.hitler.entity.PayPlatform;


 /**
  * 第三方支付平台对应支付方式配置DTO
  * @author klp
  *
  */
public class PayPlatformBankCreateDTO extends TransientDTO<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3117705697225652144L;

	private  Integer id;

	private String createdBy;

	private String createdDate;

	private String lastModifiedBy;

	private String lastModifiedDate;

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
	
	 
	
}

