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

import com.hitler.entity.PayTenantLimit;
import com.hitler.service.IPayTenantLimitService;

/**
 * 接入商户第三方支付限额表
 * 
 * @author klp
 * @version 创建时间：2017年5月4日 下午5:37:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/web-ctx.xml" })
@FixMethodOrder(MethodSorters.DEFAULT) // 执行方法顺序 删除方法放最后
public class IPayTenantLimitServiceTest {

	@Resource
	private IPayTenantLimitService payTenantLimitService;

	private PayTenantLimit payTenantLimit = null;// 初始实体
	private PayTenantLimit test_PayTenantLimit = null;// 测试后返回实体
	static int id = 0;// 测试使用id

	@Before
	public void setUp() throws Exception {
		payTenantLimit = new PayTenantLimit();
		payTenantLimit.setCreatedBy("test1");
		payTenantLimit.setCreatedDate(new Date());
		payTenantLimit.setLastModifiedBy("test1");
		payTenantLimit.setLastModifiedDate(new Date());
		payTenantLimit.setTenantId(1);
		payTenantLimit.setDailyRechargeAmountMax(0.1);
		payTenantLimit.setDailyRechargeTimesMax(1);
		payTenantLimit.setOnetimeRechargeAmountMax(0.1);
		payTenantLimit.setOnetimeRechargeAmountMin(0.1);
		payTenantLimit.setPlatformId(1);
		payTenantLimit.setPlatformCode("test1");
	}

	@Test
	public void testSaveS() {

		try {
			test_PayTenantLimit = payTenantLimitService.save(payTenantLimit);
			id = test_PayTenantLimit.getId();
			Assert.assertEquals(test_PayTenantLimit.getPlatformCode(), "test1");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}

	}

	@Test
	public void testUpdateS() {

		payTenantLimit.setId(id);
		payTenantLimit.setPlatformCode("new_test1");
		try {

			test_PayTenantLimit = payTenantLimitService.update(payTenantLimit);
			Assert.assertEquals(test_PayTenantLimit.getPlatformCode(), "new_test1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testFindPK() {

		try {

			test_PayTenantLimit = payTenantLimitService.find(id);
			Assert.assertNotNull(test_PayTenantLimit);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testFindAll() {

		try {

			List<PayTenantLimit> list = payTenantLimitService.findAll();
			Assert.assertTrue(list.size() >= 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDeletePK() {
		try {
			payTenantLimitService.delete(id);
			test_PayTenantLimit = payTenantLimitService.find(id);
			Assert.assertNull(test_PayTenantLimit);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

}
