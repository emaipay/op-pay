package com.hitler.core.utils;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang.StringUtils {

	public static List<Integer> toIntegerList(String str, String regex) {
		StringTokenizer t = new StringTokenizer(str, regex);
		List<Integer> l = new ArrayList<Integer>();
		while (t.hasMoreElements()) {
			String s = t.nextToken();
			if (isNotBlank(s)) {
				l.add(Integer.parseInt(s));
			}
		}
		return l;
	}

	public static Integer[] toIntegerArray(String str, String regex) {
		List<Integer> list = toIntegerList(str, regex);
		Integer[] arr = new Integer[list.size()];
		return list.toArray(arr);
	}

	/**
	 * 校验字符串是否是不以0开头的纯数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeral(String str) {
		return str.matches("^[1-9]\\d*$");
	}

	/**
	 * 校验字符串是否整型(0开头也可以)
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String input) {
		Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
		return mer.find();
	}

	public static List<String> partition(String str, int partition) {
		List<String> parts = new ArrayList<String>();
		int len = str.length();
		for (int i = 0; i < len; i += partition) {
			parts.add(str.substring(i, Math.min(len, i + partition)));
		}
		return parts;
	}

	// (c >= 0x0391 && c <= 0xFFE5) //中文字符
	// (c>=0x0000 && c<=0x00FF) //英文字符
	public static boolean isZH(char c) {
		if (c >= 0x0391 && c <= 0xFFE5)
			return true;
		return false;
	}

	public static String[] splitStr(String targetStr) {
		String[] paraStr = null;
		if (null != targetStr && !"".equals(targetStr)) {
			paraStr = targetStr.split("\\|", -1);
		}
		return paraStr;
	}

	/**
	 * 将json字符串Unicode编码转换成中文
	 * 
	 * @param str
	 * @return
	 */
	public static String convert(String utfString) {
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;

		while ((i = utfString.indexOf("\\u", pos)) != -1) {
			sb.append(utfString.substring(pos, i));
			if (i + 5 < utfString.length()) {
				pos = i + 6;
				sb.append((char) Integer.parseInt(utfString.substring(i + 2, i + 6), 16));
			}
		}

		return sb.toString();
	}

	/**
	 * 获取当前日期和时间
	 * 
	 * @param str
	 * @return
	 */
	public static String getCurrentDateStr() {
		Date date = new Date();
		String str = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		str = df.format(date);
		return str;
	}

	public static int getTimes(String src, String dest) {
		int times = 0, index = 0;
		do {
			index = src.indexOf(dest);
			if (index >= 0) {
				times++;
				src = src.substring(index + dest.length());
			}
		} while (index != -1);

		return times;
	}

	/**
	 * 空字串
	 */
	public static final String EMPTY = "";

	/**
	 * 检测是否为空字符串或为null
	 * 
	 * @param str
	 *            待检测的字符串
	 * @return 若str为null或空字符串则返回true; 否则返加false
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() <= 0;
	}

	/**
	 * 检测是否不为空字符串且不为null
	 * 
	 * @param str
	 *            待检测的字符串
	 * @return 若str不是null且不是空字符串则返回true; 否则返加false
	 */
	public static boolean isNotEmpty(String str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * 检测是否为空格串、空字符串或null
	 * 
	 * @param str
	 *            待检测的字符串
	 * @return 若str为null或空字符串则或空格串返回true; 否则返加false
	 */
	public static boolean isBlank(String str) {
		if (isEmpty(str)) {
			return true;
		}

		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 检测是否为空格串、空字符串或null
	 * 
	 * @param str
	 *            待检测的字符串
	 * @return 若str为null或空字符串则或空格串返回false; 否则返加true
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * 
	 * 二进制转换为十六进制String
	 * 
	 * @param buff
	 *            二进制
	 * 
	 * @return 十六进制String
	 * 
	 */
	public static String byte2hex(byte[] buff) {
		if (buff == null || buff.length <= 0) {
			return EMPTY;
		}

		StringBuilder hexStr = new StringBuilder();
		String tmpStr = null;
		for (int n = 0; n < buff.length; n++) {
			tmpStr = (java.lang.Integer.toHexString(buff[n] & 0XFF));
			if (tmpStr.length() == 1) {
				hexStr.append("0");
			}
			hexStr.append(tmpStr);
		}
		return hexStr.toString().toUpperCase();
	}

	/**
	 * 用StringTokenizer分割字符串到数组
	 * 
	 * @param str
	 *            待分割字符串
	 * @param sep
	 *            分割符
	 * @return 分割后的字符串数组
	 */
	public static String[] splitTokens(String str, String sep) {
		if (isEmpty(str)) {
			return null;
		}

		StringTokenizer token = new StringTokenizer(str, sep);
		int count = token.countTokens();
		if (count < 1) {
			return null;
		}

		int i = 0;
		String[] output = new String[count];
		while (token.hasMoreTokens()) {
			output[i] = token.nextToken();
			++i;
		}
		return output;
	}

	/**
	 * 用StringTokenizer分割字符串到数组
	 * 
	 * @param str
	 *            待分割字符串
	 * @param sep
	 *            分割符
	 * @return 分割后的字符串set容器
	 */
	public static Set<String> splitToSet(String str, String sep) {
		Set<String> output = new HashSet<String>();
		if (isEmpty(str)) {
			return output;
		}

		StringTokenizer token = new StringTokenizer(str, sep);
		while (token.hasMoreTokens()) {
			output.add(token.nextToken());
		}

		return output;
	}

	/**
	 * 将容器的字符串/数组连接起来
	 * 
	 * @param collection
	 *            容器
	 * @param sep
	 *            分割符
	 * @return 字符串
	 */
	public static String joinCol(Collection<?> collection, String sep) {
		StringBuilder outStr = new StringBuilder();
		for (Object obj : collection) {
			if (obj == null) {
				continue;
			}
			if (outStr.length() > 0) {
				outStr.append(sep);
			}
			outStr.append(obj);
		}
		return outStr.toString();
	}

	/**
	 * 将容器的字符串/数组连接起来
	 * 
	 * @param set
	 *            容器
	 * @param collection
	 *            分割符
	 * @param mark
	 *            分割字符串的前后增加mark标签符
	 * @return 字符串
	 */
	public static String join(Collection<?> collection, String sep, char mark) {
		StringBuilder outStr = new StringBuilder();
		for (Object obj : collection) {
			if (obj == null) {
				continue;
			}
			if (outStr.length() > 0) {
				outStr.append(sep);
			}
			outStr.append(mark).append(obj).append(mark);
		}
		return outStr.toString();
	}

	/**
	 * 对空串的处理
	 * 
	 * @param input
	 *            ：输入的字符串
	 * @param def
	 *            ：为空返回值
	 * @return 如果输入null或空字符串返回def，否则返回trim后字符串
	 */
	public static String handleNull(String input, String def) {
		if (isEmpty(input)) {
			return def;
		}

		String trimStr = input.trim();
		if (isEmpty(trimStr)) {
			return def;
		}

		return trimStr;
	}

	/**
	 * 判断两个字符串是否相等（大小写敏感）
	 * 
	 * <pre>
	 * StringHelper.equals(null, null)   = true
	 * StringHelper.equals(null, &quot;abc&quot;)  = false
	 * StringHelper.equals(&quot;abc&quot;, null)  = false
	 * StringHelper.equals(&quot;abc&quot;, &quot;abc&quot;) = true
	 * StringHelper.equals(&quot;abc&quot;, &quot;ABC&quot;) = false
	 * </pre>
	 */
	public static boolean equals(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	/**
	 * 判断两个字符串是否相等（不区分大小写）
	 * 
	 * <pre>
	 * StringHelper.equalsIgnoreCase(null, null)   = true
	 * StringHelper.equalsIgnoreCase(null, &quot;abc&quot;)  = false
	 * StringHelper.equalsIgnoreCase(&quot;abc&quot;, null)  = false
	 * StringHelper.equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
	 * StringHelper.equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
	 * </pre>
	 */
	public static boolean equalsIgnoreCase(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
	}

	/**
	 * 去掉字符串中所有的空格/回车/TAB
	 * 
	 * @param str
	 *            输入的字符串
	 * @return 去掉字符串中所有空格/回车/TAB的字符串
	 */
	public static String trimAll(String str) {
		if (isEmpty(str)) {
			return str;
		}

		StringBuffer buf = new StringBuffer(str);
		int index = 0;
		while (buf.length() > index) {
			char c = buf.charAt(index);
			if (Character.isWhitespace(c) || c == '\t' || c == '\r' || c == '\n') {
				buf.deleteCharAt(index);
			} else {
				++index;
			}
		}

		return buf.toString();
	}

	/**
	 * 去掉字符串中前后的空格/回车/TAB
	 * 
	 * @param str
	 *            输入的字符串
	 * @return 去掉前后空格/回车/TAB的字符串
	 */
	public static String trimMore(String str) {
		if (isEmpty(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);

		// 去掉头部的空格
		int index = 0;
		while (buf.length() > index) {
			char c = buf.charAt(index);
			if (Character.isWhitespace(c) || c == '\t' || c == '\r' || c == '\n') {
				buf.deleteCharAt(index);
			} else {
				break;
			}
		}

		// 去掉尾部的空格
		while (buf.length() > 0) {
			int len = buf.length() - 1;
			char c = buf.charAt(len);
			if (Character.isWhitespace(c) || c == '\t' || c == '\r' || c == '\n') {
				buf.deleteCharAt(len);
			} else {
				break;
			}
		}

		return buf.toString();
	}

	/**
	 * 去掉字符串中前后的空格
	 * 
	 * @param str
	 *            输入的字符串
	 * @return 返回去空格后的字符串
	 */
	public static String trim(String str) {
		if (isEmpty(str)) {
			return str;
		}
		return str.trim();
	}

	/**
	 * 第一个字母转大写
	 * 
	 * @param str
	 *            输入的字符串
	 * @return 首字母为大写的字符串
	 */
	public static String first2Upper(String str) {
		if (isEmpty(str)) {
			return str;
		}
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}

	/**
	 * 第一个字母变小写
	 * 
	 * @param str
	 *            输入的字符串
	 * @return 首字母为小写的字符串
	 */
	public static String first2Lower(String str) {
		if (isEmpty(str)) {
			return str;
		}
		return Character.toLowerCase(str.charAt(0)) + str.substring(1);
	}

	/**
	 * 字符串转int
	 * 
	 * @param s
	 * @return
	 */
	public static int StringToInt(String s) {
		if (s == null)
			return 0;
		s = s.replaceAll("[\\D]+", "");
		return Integer.parseInt(s);
	}

	public static void appendParam(StringBuilder sb, String name, String val, boolean and, String charset) {
		if (and)
			sb.append("&");
		else
			sb.append("?");
		sb.append(name);
		sb.append("=");
		if (val == null)
			val = "";
		if (StringUtils.isEmpty(charset))
			sb.append(val);
		else
			sb.append(encode(val, charset));
	}
	
	  public static String encode(String str, String charset)
	    {
	        try {
	            return URLEncoder.encode(str, charset);
	        } catch (Exception e) {
	            System.out.println(e);
	            return str;
	        }
	    }

}
