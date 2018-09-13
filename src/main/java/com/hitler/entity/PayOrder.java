package com.hitler.entity;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import com.hitler.core.entity.CheckableEntity;
import com.hitler.core.entity.annotion.Checked;
import com.hitler.core.enums.PersistEnum;
import com.hitler.core.utils.BillNoUtils;
import com.hitler.entity.PayMerchant.TerminalType;

/**
 * 在线支付充值订单表
 * 
 * @author
 *
 */
@Entity
//@DynamicInsert
//@DynamicUpdate
@Table(name = "t_pay_order", indexes = { @Index(name = "IDX_BILLNO", columnList = "BILLNO"),
		@Index(name = "IDX_USER_ID", columnList = "USER_ID"),
		@Index(name = "IDX_TRANS_BILLNO", columnList = "TRANS_BILLNO"),
		@Index(name = "IDX_MERCHANT_ID", columnList = "MERCHANT_ID"), })
public class PayOrder extends CheckableEntity<Integer> {

	private static final long serialVersionUID = -5476275880381384937L;

	/**
	 * 订单号
	 */
	@Checked
	@Column(name = "BILLNO", columnDefinition = "varchar(24) COMMENT '订单号'", nullable = false)
	private String billNo = BillNoUtils.generateBillNo(BillNoUtils.PREFIX_PR);

	/**
	 * 第三方支付单号
	 */
	@Checked
	@Column(name = "TRANS_BILLNO", columnDefinition = "varchar(24) COMMENT '第三方支付单号'", nullable = false)
	private String transBillNo;

	@Checked
	@Column(name = "user_id", columnDefinition = "int(11) COMMENT '充值用户id'", nullable = false)
	private Integer userId;

	/**
	 * 商户ID
	 */
	@Checked
	@ManyToOne
	@JoinColumn(name = "MERCHANT_ID", referencedColumnName = "id", columnDefinition = "INT COMMENT '商户ID'", nullable = false)
	private PayMerchant merchantId;

	/**
	 * 订单金额
	 */
	@Checked
	@Column(name = "ORDER_AMOUNT", columnDefinition = "DECIMAL(15,5) DEFAULT 0.0 COMMENT '订单金额'")
	private Double orderAmount;
	/**
	 * 实际金额
	 */
	@Checked
	@Column(name = "FACT_AMOUNT", columnDefinition = "DECIMAL(15,5) DEFAULT 0.0 COMMENT '实际金额'")
	private Double factAmount = 0D;
	/**
	 * 手续费
	 */
	@Checked
	@Column(name = "FEE", columnDefinition = "DECIMAL(15,5) DEFAULT 0.0 COMMENT '手续费'")
	private Double fee = 0D;

	/**
	 * 付款银行
	 */
	@Checked
	@ManyToOne
	@JoinColumn(name = "PAYER_BANK_ID", referencedColumnName = "id", columnDefinition = "INT COMMENT '付款银行'", nullable = false)
	private PayBank payerBankId;
	/**
	 * 充值信息加密
	 */
	// MD5(USER_NAME+ORDER_AMOUNT+BILLNO)
	@Checked
	@Column(name = "PAYER_SECRET", columnDefinition = "varchar(32) COMMENT 'SECRET'", nullable = false)
	private String payerSecret;

	/**
	 * 订单状态
	 */
	@Checked
	@Column(name = "ORDER_STATUS", columnDefinition = "tinyint COMMENT '订单状态'")
	@Type(type = "com.hitler.core.enums.MyEnumType")
	private OrderStatus orderStatus = OrderStatus.未付款;

	/**
	 * 支付结果描述
	 */
	@Checked
	@Column(name = "ORDER_STATUS_DESC", columnDefinition = "varchar(100) COMMENT '支付结果描述'")
	private String orderStatusDesc;
	/**
	 * 支付类型
	 */
	@Checked
	@ManyToOne
	@JoinColumn(name = "platform_id", referencedColumnName = "id", columnDefinition = "INT(11) COMMENT '支付类型'", nullable = false)
	private PayPlatform platformId;
	@Checked
	@ManyToOne
	@JoinColumn(name = "tenant_Id", referencedColumnName = "id", columnDefinition = "int(11) COMMENT '接入平台Id'", nullable = false)
	private PayTenant tenantId;
	@Checked
	@Column(name = "source_user_ID", columnDefinition = "int(11) COMMENT '充值用户id'", nullable = false)
	private Integer sourceUserId;
	@Checked
	@Column(name = "notify_url", columnDefinition = "varchar(200) COMMENT '异步回调地址'", nullable = false)
	private String notifyUrl;
	@Checked
	@Column(name = "return_url", columnDefinition = "varchar(200) COMMENT '页面回调地址'", nullable = false)
	private String returnUrl;
	@Column(name = "user_name", columnDefinition = "varchar(15) COMMENT '回调地址'", nullable = true)
	private String userName;
	@Checked
	@Column(name = "Req_Reserved", columnDefinition = "varchar(512) COMMENT '请求保留信息'", nullable = true)
	private String reqReserved;
	@Checked
	@Column(name = "summary", columnDefinition = "varchar(256) COMMENT '描述'", nullable = false)
	private String summary;
	
	@Column(name="terminal_Type" ,columnDefinition="tinyint comment '接入终端类型'")
	private TerminalType terminalType;
	
	@Checked
	@Column(name = "openid", columnDefinition = "varchar(32) COMMENT '微信openid'", nullable = false)
	private String openid;
	
	/**
	 * 订单号
	 */
	@Checked
	@Column(name = "CONN_BILLNO", columnDefinition = "varchar(24) COMMENT '接入平台的订单号'", nullable = false)
	private String connBillno ;
	
	/**
	 * 第三方支付单号
	 */
	@Checked
	@Column(name = "transaction_id", columnDefinition = "varchar(128) COMMENT '接入方返回的订单号'", nullable = true)
	private String transactionId;

	
	public PayOrder() {
	}

	public static enum OrderStatus implements PersistEnum<OrderStatus> {
		未付款("0"), 支付成功("1"), 支付失败("2");
		private final String value;

		OrderStatus(String value) {
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

	public PayMerchant getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(PayMerchant merchantId) {
		this.merchantId = merchantId;
	}

	public String getPayerSecret() {
		return payerSecret;
	}

	public void setPayerSecret(String payerSecret) {
		this.payerSecret = payerSecret;
	}

	public PayBank getPayerBankId() {
		return payerBankId;
	}

	public void setPayerBankId(PayBank payerBankId) {
		this.payerBankId = payerBankId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getTransBillNo() {
		return transBillNo;
	}

	public void setTransBillNo(String transBillNo) {
		this.transBillNo = transBillNo;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Double getFactAmount() {
		return factAmount;
	}

	public void setFactAmount(Double factAmount) {
		this.factAmount = factAmount;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}

	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}

	/*********************************
	 * 枚举 2015-05-08
	 ***********************************/
	public static enum PaymentRechargeOrder_ {
		billNo("billNo"), transBillNo("transBillNo"), bankBillNo("bankBillNo"), platformName(
				"platformName"), merchantNo("merchantNo"), merchantName("merchantName"), orderAmount(
						"orderAmount"), factAmount("factAmount"), fee("fee"), signature("signature"), payerBankName(
								"payerBankName"), orderStatus("orderStatus"), createdDate("createdDate");

		private final String _name;

		PaymentRechargeOrder_(String _name) {
			this._name = _name;
		}

		public String getName() {
			return _name;
		}
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	

	public PayPlatform getPlatformId() {
		return platformId;
	}

	public void setPlatformId(PayPlatform platformId) {
		this.platformId = platformId;
	}

	

	

	public PayTenant getTenantId() {
		return tenantId;
	}

	public void setTenantId(PayTenant tenantId) {
		this.tenantId = tenantId;
	}

	public Integer getSourceUserId() {
		return sourceUserId;
	}

	public void setSourceUserId(Integer sourceUserId) {
		this.sourceUserId = sourceUserId;
	}

	

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getReqReserved() {
		return reqReserved;
	}

	public void setReqReserved(String reqReserved) {
		this.reqReserved = reqReserved;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public TerminalType getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(TerminalType terminalType) {
		this.terminalType = terminalType;
	}

	public String getConnBillno() {
		return connBillno;
	}

	public void setConnBillno(String connBillno) {
		this.connBillno = connBillno;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
