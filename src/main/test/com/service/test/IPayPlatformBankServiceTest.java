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

import com.hitler.entity.PayPlatformBank;
import com.hitler.service.IPayPlatformBankService;

/**
 * 第三方支付平台对应支付方式配置表
 * 
 * @author klp
 * @version 创建时间：2017年5月4日 下午5:37:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/web-ctx.xml" })
@FixMethodOrder(MethodSorters.DEFAULT) // 执行方法顺序 删除方法放最后
public class IPayPlatformBankServiceTest {

	@Resource
	private IPayPlatformBankService payPlatformBankService;

	private PayPlatformBank payPlatformBank = null;// 初始实体
	private PayPlatformBank test_PayPlatformBank = null;// 测试后返回实体
	static int id = 0;// 测试使用id

	@Before
	public void setUp() throws Exception {
		payPlatformBank = new PayPlatformBank();
		payPlatformBank.setCreatedBy("test1");
		payPlatformBank.setCreatedDate(new Date());
		payPlatformBank.setLastModifiedBy("test1");
		payPlatformBank.setLastModifiedDate(new Date());
		payPlatformBank.setPayCode("test1");
//		payPlatformBank.setBankId(1);
//		payPlatformBank.setPayPlatformId(1);
	}

	@Test
	public void testSaveS() {
		
	  	try {
			test_PayPlatformBank = payPlatformBankService.save(payPlatformBank);
            id = test_PayPlatformBank.getId();
			Assert.assertEquals(test_PayPlatformBank.getPayCode(), "test1");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}

	}

	@Test
	public void testUpdateS() {

	   payPlatformBank.setId(id);
	   payPlatformBank.setPayCode("new_test1");
		try {

			test_PayPlatformBank = payPlatformBankService.update(payPlatformBank);
			Assert.assertEquals(test_PayPlatformBank.getPayCode(), "new_test1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testFindPK() {

		try {

			test_PayPlatformBank = payPlatformBankService.find(id);
			Assert.assertNotNull(test_PayPlatformBank);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testFindAll() {

		try {

			List<PayPlatformBank> list = payPlatformBankService.findAll();
			Assert.assertTrue(list.size() >= 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDeletePK() {
		try {
			payPlatformBankService.delete(id);
			test_PayPlatformBank = payPlatformBankService.find(id);
			Assert.assertNull(test_PayPlatformBank);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

}
