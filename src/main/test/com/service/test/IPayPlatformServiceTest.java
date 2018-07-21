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

import com.hitler.entity.PayPlatform;
import com.hitler.entity.PayPlatformType;
import com.hitler.service.IPayPlatformService;

/**
 * 第三方支付平台管理
 * 
 * @author klp
 * @version 创建时间：2017年5月4日 下午5:37:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/web-ctx.xml" })
@FixMethodOrder(MethodSorters.DEFAULT) // 执行方法顺序 删除方法放最后
public class IPayPlatformServiceTest {

	@Resource
	private IPayPlatformService payPlatformService;

	private PayPlatformType payPlatformType = null;
	private PayPlatform payPlatform = null;// 初始实体
	private PayPlatform test_PayPlatform = null;// 测试后返回实体
	static int id = 0;// 测试使用id

	@Before
	public void setUp() throws Exception {
		payPlatform = new PayPlatform();
		payPlatform.setCreatedBy("test1");
		payPlatform.setCreatedDate(new Date());
		payPlatform.setLastModifiedBy("test1");
		payPlatform.setLastModifiedDate(new Date());
		payPlatform.setName("test1");
		payPlatform.setPayUrl("test1");
		payPlatform.setPlatformCode("test1");
		payPlatform.setPlatformLogo("test1");
		payPlatformType = new PayPlatformType();
		payPlatformType.setId(1);
		payPlatform.setPlatformTypeId(payPlatformType);
	}

	@Test
	public void testSaveS() {

		try {
			test_PayPlatform = payPlatformService.save(payPlatform);

			id = test_PayPlatform.getId();
			Assert.assertEquals(test_PayPlatform.getName(), "test1");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}

	}

	@Test
	public void testUpdateS() {

		payPlatform.setId(id);
		payPlatform.setName("new_test1");

		try {

			test_PayPlatform = payPlatformService.update(payPlatform);
			Assert.assertEquals(test_PayPlatform.getName(), "new_test1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testFindPK() {

		try {

			test_PayPlatform = payPlatformService.find(id);
			Assert.assertNotNull(test_PayPlatform);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testFindAll() {

		try {

			List<PayPlatform> list = payPlatformService.findAll();
			Assert.assertTrue(list.size() >= 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDeletePK() {
		try {
			payPlatformService.delete(id);
			test_PayPlatform = payPlatformService.find(id);
			Assert.assertNull(test_PayPlatform);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

}
