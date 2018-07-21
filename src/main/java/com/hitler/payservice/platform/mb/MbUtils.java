package com.hitler.payservice.platform.mb;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.hitler.core.utils.StringUtils;

public class MbUtils {
	
	public static String transact(String paramsStr, String serverUrl)
			throws Exception {

		if (StringUtils.isBlank(serverUrl) || StringUtils.isBlank(paramsStr)) {
			throw new NullPointerException("请求地址或请求数据为空!");
		}

		myX509TrustManager xtm = new myX509TrustManager();
		myHostnameVerifier hnv = new myHostnameVerifier();
		ByteArrayOutputStream respData = new ByteArrayOutputStream();

		byte[] b = new byte[8192];
		String result = "";
		try {
			SSLContext sslContext = null;
			try {
				sslContext = SSLContext.getInstance("TLS");
				X509TrustManager[] xtmArray = new X509TrustManager[] { xtm };
				sslContext.init(null, xtmArray,
						new java.security.SecureRandom());
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}

			if (sslContext != null) {
				HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
						.getSocketFactory());
			}
			HttpsURLConnection.setDefaultHostnameVerifier(hnv);

			// 匹配http或者https请求
			URLConnection conn = null;
			if (serverUrl.toLowerCase().startsWith("https")) {
				HttpsURLConnection httpsUrlConn = (HttpsURLConnection) (new URL(
						serverUrl)).openConnection();
				httpsUrlConn.setRequestMethod("POST");
				conn = httpsUrlConn;
			} else {
				HttpURLConnection httpUrlConn = (HttpURLConnection) (new URL(
						serverUrl)).openConnection();
				httpUrlConn.setRequestMethod("POST");
				conn = httpUrlConn;
			}

			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.1) Gecko/20061204 Firefox/2.0.0.3");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("connection", "close");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.getOutputStream().write(paramsStr.getBytes("utf-8"));
			conn.getOutputStream().flush();

			int len = 0;
			try {
				while (true) {
					len = conn.getInputStream().read(b);
					if (len <= 0) {
						conn.getInputStream().close();
						break;
					}
					respData.write(b, 0, len);
				}
			} catch (SocketTimeoutException ee) {
				throw new RuntimeException("读取响应数据出错！" + ee.getMessage());
			}

			// 返回给商户的数据
			result = respData.toString("utf-8");
			if (StringUtils.isBlank(result)) {
				throw new RuntimeException("返回参数错误！");
			}
		} catch (Exception e) {
			throw new RuntimeException("发送POST请求出现异常！" + e.getMessage());
		}

		// 验签返回数据后返回支付平台回复数据
		checkResult(result);
		return result;
	}
	
	/**
	 * 如果数据被篡改 则抛出异常
	 * 
	 * @param result
	 * @throws Exception
	 */
	private static void checkResult(String result) throws Exception {

		if (StringUtils.isBlank(result)) {
			throw new NullPointerException("返回数据为空!");
		}

		try {
			Document resultDOM = DocumentHelper.parseText(result);
			Element root = resultDOM.getRootElement();
			String responseData = root.element("respData").asXML();
			String signMsg = root.element("signMsg").getStringValue();

			if (StringUtils.isBlank(responseData)
					|| StringUtils.isBlank(signMsg)) {
				throw new Exception("解析返回验签或原数据错误！");
			}

		} catch (DocumentException e) {
			throw new RuntimeException("xml解析错误：" + e);
		}
	}
	
}
class myX509TrustManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] chain, String authType) {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}

class myHostnameVerifier implements HostnameVerifier {

	public boolean verify(String hostname, SSLSession session) {
		return true;
	}

}
