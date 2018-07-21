package com.hitler.core.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
/**
 * xml转换类
 * @author xujw
 * 2016-12-08
 */
@SuppressWarnings("all")
public class XmlUtil {
	
	/**
	 * xml转element
	 * @param xml
	 * @return
	 */
	public static Element xml2Ele(String xml) throws DocumentException{
		Document doc = xml2Doc(filterChars(xml));
		System.out.println(doc.getRootElement().getName());
		return doc.getRootElement();
	}
	
	public static Document xml2Doc(String xml) throws DocumentException{
		System.out.println(filterChars(xml));
		return DocumentHelper.parseText(filterChars(xml));
	}
	
	public static String filterChars(String text){
		if(text==null){
			return text;
		}
		int size=text.length();
		if(size<0){
			return text;
		}
		StringBuilder sb=new StringBuilder(size);
		for(int i=0;i<size;i++){
			char c=text.charAt(i);
			// 保留字符
			if (c == '<' || c == '>' || c == '&' || c == '\t' || c == '\n'
					|| c == '\r') {
				sb.append(c);
				continue;
			}
			if(c<32){
				// 过滤掉[0~31]的不可见字符
				continue;
			}
			int v = (int) c;
			if (com.sun.org.apache.xerces.internal.util.XMLChar
					.isInvalid((int) c)) {// 无效字符
				if (com.sun.org.apache.xerces.internal.util.XMLChar
						.isHighSurrogate(v)) {
					// 如果Unicode高低位匹配的就不过滤
					if (i + 1 < size) {
						char lowC = text.charAt(i + 1);
						int low = (int) lowC;
						if (com.sun.org.apache.xerces.internal.util.XMLChar
								.isLowSurrogate(low)) {
							// 找到低位
							sb.append(c).append(lowC);
							++i;
							continue;
						}
					}
				}
				continue;
			}
			// 保留字符
			sb.append(c);
		}
		return sb.toString();
	}

}
