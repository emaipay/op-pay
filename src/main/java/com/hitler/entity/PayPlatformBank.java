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
@Table(name="t_pay_platform_bank")
public class PayPlatformBank extends CheckableEntity<Integer> {

	private static final long serialVersionUID = 4413838503067673045L;
	@Checked
	@Column(name = "pay_code", columnDefinition = "varchar(6) comment '第三方支付支付方式code'", nullable = false)
	private String payCode;
	@ManyToOne
	@JoinColumn(name = "bank_id", referencedColumnName="id",columnDefinition = "int(11) comment '银行ID,对应tb_pay_bank", nullable = false)
	private PayBank bankId;
	@ManyToOne
	@JoinColumn(name = "pay_platform_id", referencedColumnName="id", columnDefinition = "int(11) comment '支付类型ID,对应tb_pay_platform'", nullable = false)
	private PayPlatform payPlatformId;

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
