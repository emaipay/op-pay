package com.hitler.dto;

import com.hitler.core.dto.TransientDTO;
import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayTenant;

/**
 * 接入商户对应第三方支付商户 Dto
 * @author klp
 *
 */
public class PayTenantMerchantDTO extends TransientDTO<Integer>{
	
	private static final long serialVersionUID = 5981614200766038244L;

	private Integer id;
	
	private PayMerchant memberId;

	private PayTenant terminalId;

	private String platformName;
	
	private String memberDomain;


	 

	public PayMerchant getMemberId() {
		return memberId;
	}


	public void setMemberId(PayMerchant memberId) {
		this.memberId = memberId;
	}


	public PayTenant getTerminalId() {
		return terminalId;
	}


	public void setTerminalId(PayTenant terminalId) {
		this.terminalId = terminalId;
	}


	public String getPlatformName() {
		return platformName;
	}


	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}


	public void setId(Integer id) {
		this.id = id;
	}


 
	public Integer getId() {
		// TODO Auto-generated method stub
		return id;
	}


	public String getMemberDomain() {
		return memberDomain;
	}


	public void setMemberDomain(String memberDomain) {
		this.memberDomain = memberDomain;
	}
	

}
