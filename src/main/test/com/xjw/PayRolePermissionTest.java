package com.xjw;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hitler.entity.PayPlatformType;
import com.hitler.service.IPayPlatformTypeService;

/**
 * 类说明
 * 
 * @author klp
 * @version 创建时间：2017年5月4日 下午2:23:18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/web-ctx.xml" })
public class PayRolePermissionTest {

	@Resource
	private IPayPlatformTypeService payPlatformTypeService;

	@Test
	public void test() {
		PayPlatformType pt = new PayPlatformType();
		 pt.setId(5);
		pt.setCreatedBy("test1");
		pt.setCreatedDate(new Date());
		pt.setPlatformType("test1");
		pt.setTypeName("test1");
		try {

			payPlatformTypeService.update(pt);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
