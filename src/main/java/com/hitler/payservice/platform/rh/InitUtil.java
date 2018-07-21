package com.hitler.payservice.platform.rh;

import com.hitler.core.utils.PreProperties;
import com.lechinepay.channel.lepay.client.apppay.AppPay;

/**
 * 初始化
 * 
 * @author nicholas
 *
 */
public class InitUtil {

	public static void init(String merKey) {
		PreProperties pro=new PreProperties("ctx.properties");
		String keyStorePath = pro.getProperty("LEPAY_PFX_URL");
		String certificatePath = pro.getProperty("LEPAY_CER_URL");
		// +Lepay公钥证书
		String keyStoreType = "PKCS12";
		
		AppPay.init(merKey, keyStorePath, keyStoreType, certificatePath);
		
	}

	public static void initTest() {
		String keyStorePassword = "cXdlMTIz";
		String keyStorePath = "D:/payapi/lepay/appapi/src/main/resources/test/testclient1.pfx";// 商户私钥证书
		String certificatePath = "D:/payapi/lepay/appapi/src/main/resources/test/testclient.cer";// +Lepay公钥证书
		//D:/work/hitler3.0_newsvn/Hitler3.0/hitler-opp/target/classes/testclient1.pfx
		//D:/work/hitler3.0_newsvn/Hitler3.0/hitler-opp/target/classes/pdtserver.cer
		
		String keyStoreType = "PKCS12";
		AppPay.init(keyStorePassword, keyStorePath, keyStoreType, certificatePath);
	}

	public static void main(String[] args) {
		init("cXdlMTIz");
	}
}
