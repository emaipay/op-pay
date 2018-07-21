package com.hitler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hitler.core.entity.CheckableEntity;
import com.hitler.core.entity.annotion.Checked;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_pay_tenant")
public class PayTenant extends CheckableEntity<Integer> {

	private static final long serialVersionUID = 3753557845119730550L;
	@Checked
	@Column(name = "member_id", columnDefinition = "varchar(20) comment '接入平台分配的平台号'", nullable = false)
	private String memberId;
	@Checked
	@Column(name = "terminal_id", columnDefinition = "varchar(20) comment '接入平台分配的终端号'", nullable = false)
	private String terminalId;
	@Column(name = "member_domain", columnDefinition = "varchar(100) comment '接入平台域名'", nullable = true)
	private String memberDomain;
	@Column(name = "platform_name", columnDefinition = "varchar(20) comment '接入平台名称'", nullable = true)
	private String platformName;
	@Column(name = "mer_key", columnDefinition = "varchar(32) comment '接入平台秘钥'", nullable = true)
	private String merKey;
	@ManyToOne
	@JoinColumn(name = "user_id", columnDefinition = "int(11) comment '用户id'", nullable = true)
	private PayUser userId;
	
	@Column(name = "user_name", columnDefinition = "varchar(16) comment '用户名'", nullable = true)
	private String userName;
	

	@Checked
	@Column(name = "AVAILABLE", columnDefinition="TINYINT(2) COMMENT '是否可用'")
	private Boolean available = Boolean.TRUE;
	
	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getMemberDomain() {
		return memberDomain;
	}

	public void setMemberDomain(String memberDomain) {
		this.memberDomain = memberDomain;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
