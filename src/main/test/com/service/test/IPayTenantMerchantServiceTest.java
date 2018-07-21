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
import com.hitler.entity.PayTenant;
import com.hitler.entity.PayTenantMerchant;
import com.hitler.service.IPayTenantMerchantService;

/**
 * 接入商户对应第三方支付商户表
 * 
 * @author klp
 * @version 创建时间：2017年5月4日 下午5:37:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/web-ctx.xml" })
@FixMethodOrder(MethodSorters.DEFAULT) // 执行方法顺序 删除方法放最后
public class IPayTenantMerchantServiceTest {

	@Resource
	private IPayTenantMerchantService payTenantMerchantService;

	private PayTenant payTenant = null;
	private PayMerchant payMerchant = null;
	private PayTenantMerchant payTenantMerchant = null;// 初始实体
	private PayTenantMerchant test_PayTenantMerchant = null;// 测试后返回实体
	static int id = 0;// 测试使用id

	@Before
	public void setUp() throws Exception {
		payTenantMerchant = new PayTenantMerchant();
		payTenantMerchant.setCreatedBy("test1");
		payTenantMerchant.setCreatedDate(new Date());
		payTenantMerchant.setLastModifiedBy("test1");
		payTenantMerchant.setLastModifiedDate(new Date());
		payTenant = new PayTenant();
		payTenant.setId(1);
		payTenantMerchant.setTenantId(payTenant);
		payMerchant = new PayMerchant();
		payMerchant.setId(1);
		payTenantMerchant.setPayMerchantId(payMerchant);
	}

	@Test
	public void testSaveS() {

		try {
			test_PayTenantMerchant = payTenantMerchantService.save(payTenantMerchant);
			id = test_PayTenantMerchant.getId();
			Assert.assertNotNull(test_PayTenantMerchant);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}

	}

	@Test
	public void testUpdateS() {

		payTenantMerchant.setId(id);
		payTenantMerchant.setCreatedBy("new_test1");
		try {

			test_PayTenantMerchant = payTenantMerchantService.update(payTenantMerchant);
			Assert.assertEquals(test_PayTenantMerchant.getCreatedBy(), "new_test1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testFindPK() {

		try {

			test_PayTenantMerchant = payTenantMerchantService.find(id);
			Assert.assertNotNull(test_PayTenantMerchant);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testFindAll() {

		try {

			List<PayTenantMerchant> list = payTenantMerchantService.findAll();
			Assert.assertTrue(list.size() >= 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDeletePK() {
		try {
			payTenantMerchantService.delete(id);
			test_PayTenantMerchant = payTenantMerchantService.find(id);
			Assert.assertNull(test_PayTenantMerchant);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

}
