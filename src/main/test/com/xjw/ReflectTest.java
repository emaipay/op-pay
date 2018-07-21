package com.xjw;

import org.springframework.util.ReflectionUtils;

public class ReflectTest {
	
	public static void main(String[] args) {
		try {
			Class<?> cls=Class.forName("com.hitler.thirdpay.MfPayService");
			ReflectionUtils.findMethod(cls,"getPayPage",new Class<?>[]{});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
