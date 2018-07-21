package com.hitler.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class HttpReqUtil {
	
	/**
	 * 获取请求内容
	 * 
	 * @param inputStream
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String inputStreamToStrFromByte(InputStream inputStream) {
		// 读取请求内容
		StringBuilder sb = null;
		String str = null;
		try {
			int a = 0;
			byte[] bytes = new byte[2048];
			sb = new StringBuilder();
			while ((a = inputStream.read(bytes)) != -1) {
				sb.append(new String(bytes, 0, a, "utf-8"));
			}
			str = URLDecoder.decode(sb.toString(), "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;// sb.toString();
	}

}
