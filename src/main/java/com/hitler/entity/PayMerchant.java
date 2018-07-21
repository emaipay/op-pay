package com.hitler.entity	;

import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hitler.core.entity.CheckableEntity;
import com.hitler.core.entity.annotion.Checked;
import com.hitler.core.enums.PersistEnum;
import com.hitler.entity.PayOrder.OrderStatus;
/**
 * 支付商户 modal
 * @author
 *
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_pay_merchant", indexes = { 
		@Index(name = "IDX_MERCHANT_NO", columnList = "MERCHANT_NO")
	})
public class PayMerchant extends CheckableEntity<Integer> {

	private static final long serialVersionUID = -8872254051574418211L;
	
	/**
	 * 支付平台ID
	 */
	@Checked
	@ManyToOne
	@JoinColumn(name = "PLATFORM_ID", referencedColumnName = "id", columnDefinition="INT COMMENT '支付平台ID'", nullable = false)
	private PayPlatform platformId;
	
	/**
	 * 商户号
	 */
	@Checked
	@Column(name = "MERCHANT_NO", columnDefinition="varchar(10) COMMENT '商户号'", nullable = false)
	private String merchantNo;
	
	/**
	 * 终端号
	 */
	@Checked
	@Column(name = "TERMINAL_NO", columnDefinition="varchar(10) COMMENT '终端号'", nullable = false)
	private String terminalNo;
	
	/**
	 * 商户名称
	 */
	@Checked
	@Column(name = "MERCHANT_NAME", columnDefinition="varchar(10) COMMENT '商户名称'", nullable = false)
	private String merchantName;
	
	/**
	 * 密钥
	 */
	@Checked
	@Column(name = "MERCHANT_KEY", columnDefinition="varchar(100) COMMENT '密钥'", nullable = false)
	private String key;
	
	/**
	 * 手续费比例
	 */
	@Checked
	@Column(name = "FEE_PERCENT", columnDefinition="DECIMAL(15,5) DEFAULT 0.0 COMMENT '手续费比例'")
	private Double feePercent = 0d;
	
	/**
	 * 卡上余额
	 */
	@Checked
	@Column(name = "CURRENT_BALANCE", columnDefinition="DECIMAL(15,5) DEFAULT 0.0 COMMENT '卡上余额'")
	private Double currentBalance = 0D;
	
	/**
	 * 预警金额
	 */
	@Checked
	@Column(name = "WARNING_AMOUNT", columnDefinition="DECIMAL(15,5) DEFAULT 0.0 COMMENT '预警金额'")
	private Double warningAmount = 0D;
	
	/**
	 * 是否可用
	 */
	@Checked
	@Column(name = "AVAILABLE", columnDefinition="TINYINT(2) COMMENT '是否可用'")
	private Boolean available = Boolean.TRUE;
	
	@Checked
	@Column(name="DOMAIN_NAME" ,columnDefinition="varchar(100) COMMENT '绑定的二级域名'")
	private String domainName;
	
	
	@Checked
	@Column(name="PUBLIC_KEY" ,columnDefinition="varchar(1000) comment '商户公钥字段'")
    private String publicKey;
	
	@Column(name="terminal_Type" ,columnDefinition="tinyint comment '接入终端类型'")
	private TerminalType terminalType;//0:pc  1:手机    2:ios
	
	@Checked
	@Column(name="paycode" ,columnDefinition="varchar(32) comment '支付通道'")
    private String paycode;
	
	@Checked
	@Column(name="page_type" ,columnDefinition="INT comment '0返回json数据，1页面跳转'")
    private Integer pageType;	
	
	
////	1-android客户端； 2-IOS客户端； 3-PC
////	端 （web页面） ； 4-手机端 （wap或html5
////	等页面）；
//	@Column(name="product_type" ,columnDefinition="char(1) comment '产品形态,主要是汇鑫支付用到'")
//	private String productType;
	public static enum TerminalType implements PersistEnum<OrderStatus> {
		PC("0"), 手机("1"), IOS("2");
		private final String value;

		TerminalType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		private static final Map<String, OrderStatus> map = new TreeMap<String, OrderStatus>();
		static {
			for (OrderStatus enumObj : OrderStatus.values()) {
				map.put(enumObj.getValue(), enumObj);
			}
		}

	}


	public PayPlatform getPlatformId() {
		return platformId;
	}

	public void setPlatformId(PayPlatform platformId) {
		this.platformId = platformId;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Double getFeePercent() {
		return feePercent;
	}

	public void setFeePercent(Double feePercent) {
		this.feePercent = feePercent;
	}

	public Double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Double getWarningAmount() {
		return warningAmount;
	}

	public void setWarningAmount(Double warningAmount) {
		this.warningAmount = warningAmount;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}
	
	

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public TerminalType getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(TerminalType terminalType) {
		this.terminalType = terminalType;
	}

	public String getPaycode() {
		return paycode;
	}

	public void setPaycode(String paycode) {
		this.paycode = paycode;
	}

	public Integer getPageType() {
		return pageType;
	}

	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	}

	

//	public String getProductType() {
//		return productType;
//	}
//
//	public void setProductType(String productType) {
//		this.productType = productType;
//	}
	
	
	
}
