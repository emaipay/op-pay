package com.hitler.core.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

/**
 * 服务端json转换
 * @author klp
 *
 */
@SuppressWarnings("all")
public class JsonUtil {
	private static Logger log = Logger.getLogger(JsonUtil.class);

	
	/**
	 *  将json 输出到客户端
	 * @param response
	 * @param value
	 */
	public static void writeJson(HttpServletResponse response, Object value) {
		PrintWriter out = null;
		response.setContentType("text/javascript;charset=utf-8");
		try {
			ObjectMapper om = new ObjectMapper();
			out = response.getWriter();
			om.writeValue(out, value);
			om = null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	
	/**
	 *  将Html 输出到客户端
	 * @param response
	 * @param value
	 */
	public static void writeHtml(HttpServletResponse response, Object value) {
		PrintWriter out = null;
		//response.setContentType("text/javascript;charset=utf-8");
		response.setContentType("text/html;charset=utf-8");
		try {
			//ObjectMapper om = new ObjectMapper();
			out = response.getWriter();
			//om.writeValue(out, value);
			//om = null;
			out.print(value);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	
	/***
	 * 
	 * 
	 * 将json 装换成javaBean
	 * @param <T>
	 * @param json
	 * @param bean
	 * @return
	 */
	public static <T> T convertToObject(String json, Class<T> bean) {
		try {
			ObjectMapper om = new ObjectMapper();
			 T t1=  om.readValue(json, bean);
			
			om = null;
			return t1;
		} catch (Exception e) {
			log.debug(" Json 转换失败 :  " + e.getMessage());
		}
		return null;
	}

	
	/**
	 * 快速将 json 转换成 list泛型
	 * List<User> list = convertToObject(json , Arraylist.class , User.class);
	 * 
	 * @param <T>
	 * @param json
	 * @param collectionClass
	 * @param elementClasses
	 * @return
	 */
	public static <T> T convertToObject(String json, Class collectionClass,Class elementClasses) {
		try {
			ObjectMapper om = new ObjectMapper();
			JavaType javaType = om.getTypeFactory().constructParametricType(collectionClass, elementClasses);
			T t1=  om.readValue(json, javaType);
			return t1;
		} catch (Exception e) {
			log.debug(" Json 转换失败 :  " + e.getMessage());
		}
		return null;
	}

	
	/***
	 *  javaBean  装换成 json字符串
	 * 
	 * @param value
	 * @return
	 */
	public static String convertToString(Object value) {
		String result = null;
		try {
			ObjectMapper om = new ObjectMapper();
			result = om.writeValueAsString(value);
		} catch (IOException e) {
			log.debug(" 转换Json 失败 :  " + e.getMessage());
		}
		return result;
	}

 
}
