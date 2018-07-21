package com.hitler.payservice.platform.jfk;

import java.io.IOException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@SuppressWarnings("deprecation")
public class JsonUtils {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

	static {
		objectMapper.getDeserializationConfig().set(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
	}

	/**
	 * Object 转化成json
	 * 
	 * @param o
	 *            对象值
	 * @return
	 */
	public static String objectToJson(Object o) {
		try {
			return objectMapper.writeValueAsString(o);
		} catch (IOException e) {
			logger.error("object:{} to json error:{}", o, e);
		}
		return null;
	}

	/**
	 * json 转化成 object
	 * 
	 * @param json
	 *            json数据
	 * @param className
	 * @return
	 */
	public static <T> T jsonToObject(String json, Class<T> className) {
		try {
			return (T) objectMapper.readValue(json, className);
		} catch (IOException e) {
			logger.error("json:{} to object error:{}", json, e);
		}
		return null;
	}

	/**
	 * json转化成List
	 * 
	 * @param json
	 *            json数据
	 * @param className
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jsonToObject(String json, TypeReference<T> className) {
		try {
			return (T) objectMapper.readValue(json, className);
		} catch (IOException e) {
			logger.error("json:{} to object error:{}", json, e);
		}
		return null;
	}
}
