package com.service.test;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hitler.entity.PayPlatformType;
import com.hitler.service.IPayPlatformTypeService;

/**
 * 支付类型大类表
 * 
 * @author klp
 * @version 创建时间：2017年5月4日 下午5:37:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/web-ctx.xml" })
@FixMethodOrder(MethodSorters.DEFAULT) // 执行方法顺序 删除方法放最后
public class IPayPlatformTypeServiceTest {

	@Resource
	private IPayPlatformTypeService payPlatformTypeService;

	private PayPlatformType payPlatformType = null;// 初始实体
	private PayPlatformType test_PayPlatformType = null;// 测试后返回实体
	static int id = 0;// 测试使用id

	@Before
	public void setUp() throws Exception {
		payPlatformType = new PayPlatformType();
		payPlatformType.setCreatedBy("test1");
		payPlatformType.setCreatedDate(new Date());
		payPlatformType.setPlatformType("test1");
		payPlatformType.setTypeName("test1");
	}

	@Test
	public void testSaveS() {

		try {
			test_PayPlatformType = payPlatformTypeService.save(payPlatformType);

			id = test_PayPlatformType.getId();
			Assert.assertEquals(test_PayPlatformType.getPlatformType(), "test1");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}

	}

	@Test
	public void testUpdateS() {

		payPlatformType.setId(id);
		payPlatformType.setPlatformType("new_test1");

		try {

			test_PayPlatformType = payPlatformTypeService.update(payPlatformType);
			Assert.assertEquals(test_PayPlatformType.getPlatformType(), "new_test1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testFindPK() {

		try {

			test_PayPlatformType = payPlatformTypeService.find(id);
			Assert.assertNotNull(test_PayPlatformType);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testFindAll() {

		try {

			List<PayPlatformType> list = payPlatformTypeService.findAll();
			Assert.assertTrue(list.size() >= 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDeletePK() {
		try {
			payPlatformTypeService.delete(id);
			test_PayPlatformType = payPlatformTypeService.find(id);
			Assert.assertNull(test_PayPlatformType);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

}
