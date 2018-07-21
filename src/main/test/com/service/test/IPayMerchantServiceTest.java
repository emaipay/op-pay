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

import com.hitler.entity.PayMerchant;
import com.hitler.entity.PayMerchant.TerminalType;
import com.hitler.entity.PayPlatform;
import com.hitler.service.IPayMerchantService;

/**
 * 第三方商户表
 * 
 * @author klp
 * @version 创建时间：2017年5月4日 下午5:37:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/web-ctx.xml" })
@FixMethodOrder(MethodSorters.DEFAULT) // 执行方法顺序 删除方法放最后
public class IPayMerchantServiceTest {

	@Resource
	private IPayMerchantService payMerchantService;

	private PayPlatform payPlatform = null;
	private PayMerchant payMerchant = null;// 初始实体
	private PayMerchant test_PayMerchant = null;// 测试后返回实体
	static int id = 0;// 测试使用id

	@Before
	public void setUp() throws Exception {
		payMerchant = new PayMerchant();
		payMerchant.setCreatedBy("test1");
		payMerchant.setCreatedDate(new Date());
		payMerchant.setLastModifiedBy("test1");
		payMerchant.setLastModifiedDate(new Date());
		payMerchant.setAvailable(true);
		payMerchant.setCurrentBalance(0.1);
		payMerchant.setDomainName("test1");
		payMerchant.setFeePercent(0.1);
		payMerchant.setKey("test1");
		payMerchant.setMerchantNo("test1");
		payMerchant.setMerchantName("test1");
		payMerchant.setPublicKey("test1");
		payMerchant.setTerminalNo("test1");
		payMerchant.setWarningAmount(0.1);
		payPlatform = new PayPlatform();
		payPlatform.setId(1);
		payMerchant.setPlatformId(payPlatform);
		payMerchant.setTerminalType(TerminalType.手机);
	}

	@Test
	public void testSaveS() {
		
		try {
			test_PayMerchant = payMerchantService.save(payMerchant);

			id = test_PayMerchant.getId();
			Assert.assertEquals(test_PayMerchant.getKey(), "test1");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}

	}

	@Test
	public void testUpdateS() {

		 payMerchant.setId(id);
         payMerchant.setKey("new_test1");
		 
		try {

			test_PayMerchant = payMerchantService.update(payMerchant);
			Assert.assertEquals(test_PayMerchant.getKey(), "new_test1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testFindPK() {

		try {

			test_PayMerchant = payMerchantService.find(id);
			Assert.assertNotNull(test_PayMerchant);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testFindAll() {

		try {

			List<PayMerchant> list = payMerchantService.findAll();
			Assert.assertTrue(list.size() >= 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDeletePK() {
		try {
			payMerchantService.delete(id);
			test_PayMerchant = payMerchantService.find(id);
			Assert.assertNull(test_PayMerchant);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

}
