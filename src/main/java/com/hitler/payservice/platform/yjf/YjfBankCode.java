package com.hitler.payservice.platform.yjf;

public enum YjfBankCode {
	
/*	ICBC("ICBC","ICBC_NET_B2C"),//中国工商银行
	CMB("CMB","CMBCHINA_NET_B2C"),//招商银行
	ABC("ABC","ABC_NET_B2C"),//农业银行
	CCB("CCB","CCB_NET_B2C"),//建设银行
	BCCB("BCCB","BCCB_NET_B2C"),//北京银行
	BOCO("BOCO","BOCO_NET_B2C"),//交通银行
	CIB("CIB","CIB_NET_B2C"),//兴业银行
	CMBC("CMBC","CMBC_NET_B2C"),//中国民生银行
	CEB("CEB","CEB_NET_B2C"),//光大银行
	BOC("BOC","BOC_NET_B2C"),//中国银行
	PINGANBANK("PINGANBANK","PINGANBANK_NET_B2C"),//中国平安银行
	ECITIC("ECITIC","ECITIC_NET_B2C"),//中信银行
	SDB("SDB","SDB_NET_B2C"),//深圳发展银行
	CGB("CGB","CGB_NET_B2C"),//广发银行
	SPDB("SPDB","SPDB_NET_B2C"),//上海浦东发展银行
	POST("POST","POST_NET_B2C"),//中国邮政
	HXB("HXB","HXB_NET_B2C"),//华夏银行
*/
	ICBC("ICBC","ICBC"),//中国工商银行
	CMB("CMB","CMB"),//招商银行
	ABC("ABC","ABC"),//农业银行
	CCB("CCB","CCB"),//建设银行
	BCCB("BCCB","BCCB"),//北京银行
	BOCO("BOCM","BOCM"),//交通银行
	CIB("CIB","CIB"),//兴业银行
	CMBC("CMBC","CMBC"),//中国民生银行
	CEB("CEB","CEB"),//光大银行
	BOC("BOC","BOC"),//中国银行
	PINGANBANK("PAYH","PAYH"),//中国平安银行
	ECITIC("CITIC","CITIC"),//中信银行
	SDB("SDB","SDB"),//深圳发展银行
	CGB("CGB","CGB"),//广发银行
	SPDB("SPDB","SPDB"),//上海浦东发展银行SPDB
	POST("PSBC","PSBC"),//中国邮政
	HXB("HXB","HXB"),//华夏银行
	SHBANK("SHBANK","SHBANK"),//上海银行
	//17	上海银行（Bank of Shanghai）	SHBANK
	;
	
	private String name;
	
	private String code;

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
	
	YjfBankCode(String name, String code){
		this.name = name;
		this.code = code;
	}
	
	public static String getCodeByName(String name){
		for (YjfBankCode e : YjfBankCode.values()) {
			if(e.getName().equals(name)){
				return e.getCode();
			}
		}
		return null;
	}

}
