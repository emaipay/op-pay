package com.hitler.core.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

public class BeanMapper {

	private static final Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static void map(Object source, Object destination) {
		
		mapper.map(source, destination);
	}

	public static <D> D map(Object source, Class<D> destinationClass) {
		return mapper.map(source, destinationClass);
	}

	public static <S, D> List<D> map(List<S> source, Class<D> destinationClass) {
		List<D> dest = Lists.newArrayList();
		for (S element : source) {
			dest.add(mapper.map(element, destinationClass));
		}
		return dest;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> objectToMap(Object obj) {
		return objectMapper.convertValue(obj, Map.class);
	}

	public static String objToJson(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			System.out.println("对象转换JSON 失败:" + obj.toString());
			e.printStackTrace();
		}
		return null;
	}

	
	public static <T> T jsonToObj(String json,Class<T> t){
		try {
			return  objectMapper.readValue(json,  t);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
