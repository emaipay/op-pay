package com.hitler.entity;

import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import com.hitler.core.entity.CheckableEntity;
import com.hitler.core.entity.annotion.Checked;
import com.hitler.core.enums.PersistEnum;

/**
 * @author 银行表
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_pay_bank", indexes = { @Index(name = "IDX_NAME", columnList = "NAME") })
public class PayBank extends CheckableEntity<Integer> {

	private static final long serialVersionUID = 4133849097582140477L;

	/**
	 * 银行名称
	 */
	@Checked
	@Column(name = "NAME",   columnDefinition = "varchar(10) COMMENT '银行名称'", nullable = false)
	private String name;
	
	/**
	 * 银行简称
	 */
	@Checked
	@Column(name = "SHORT_NAME",  columnDefinition = "varchar(10) COMMENT '银行简称'",nullable=false)
	private String shortName;
	
	/**
	 * 银行Logo图片路径
	 */
	@Checked
	@Column(name = "LOGO_FILE_PATH", columnDefinition = "varchar(50) COMMENT '银行Logo图片路径'")
	private String logoFilePath;


	/**
	 * 线下充值状态
	 */
	@Checked
	@Column(name = "BANK_TRANSFER_STATUS", columnDefinition = "tinyint COMMENT '线下充值状态'", nullable = false)
	@Type(type = "com.hitler.core.enums.MyEnumType")
	private BankTransferStatus bankTransferStatus=BankTransferStatus.开放;

	/**
	 * 是否允许绑定
	 */
	@Checked
	@Column(name = "ALLOW_BINDING_STATUS", columnDefinition = "tinyint COMMENT '是否允许绑定'", nullable = false)
	@Type(type = "com.hitler.core.enums.MyEnumType")
	private AllowBindingStatus allowBindingStatus;

	public static enum BankTransferStatus implements PersistEnum<BankTransferStatus> {
		开放("0"), 
		关闭("1");
		private final String value;
		BankTransferStatus(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
        private static final Map<String, BankTransferStatus> map = new TreeMap<String, BankTransferStatus>();
		static {
			for (BankTransferStatus enumObj : BankTransferStatus.values()) {
				map.put(enumObj.getValue(), enumObj);
			}
		}
		
	}

	public static enum AllowBindingStatus implements PersistEnum<AllowBindingStatus> {
		开放("0"), 
		关闭("1");
		private final String value;
		AllowBindingStatus(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
        private static final Map<String, AllowBindingStatus> map = new TreeMap<String, AllowBindingStatus>();
		static {
			for (AllowBindingStatus enumObj : AllowBindingStatus.values()) {
				map.put(enumObj.getValue(), enumObj);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogoFilePath() {
		return logoFilePath;
	}

	public void setLogoFilePath(String logoFilePath) {
		this.logoFilePath = logoFilePath;
	}

	public BankTransferStatus getBankTransferStatus() {
		return bankTransferStatus;
	}

	public void setBankTransferStatus(BankTransferStatus bankTransferStatus) {
		this.bankTransferStatus = bankTransferStatus;
	}

	public AllowBindingStatus getAllowBindingStatus() {
		return allowBindingStatus;
	}

	public void setAllowBindingStatus(AllowBindingStatus allowBindingStatus) {
		this.allowBindingStatus = allowBindingStatus;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	/*********************************
	 * 枚举 2015-05-08
	 ***********************************/
	public static enum Bank_ {
		code("code"), name("name"), url("url"), logoFilePath("logoFilePath"), rechargeDemoUrl(
				"rechargeDemoUrl"), bankTransferStatus("bankTransferStatus"), allowBindingStatus(
				"allowBindingStatus");

		private final String _name;

		Bank_(String _name) {
			this._name = _name;
		}

		public String getName() {
			return _name;
		}
	}
}
