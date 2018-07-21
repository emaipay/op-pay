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

import com.hitler.entity.PayTenant;
import com.hitler.entity.PayUser;
import com.hitler.service.IPayTenantService;

/**
 * 接入商户表
 * 
 * @author klp
 * @version 创建时间：2017年5月4日 下午5:37:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/web-ctx.xml" })
@FixMethodOrder(MethodSorters.DEFAULT) // 执行方法顺序 删除方法放最后
public class IPayTenantServiceTest {

	@Resource
	private IPayTenantService payTenantService;

	private PayUser payUser = null;
	private PayTenant payTenant = null;// 初始实体
	private PayTenant test_PayTenant = null;// 测试后返回实体
	static int id = 0;// 测试使用id

	@Before
	public void setUp() throws Exception {
		payTenant = new PayTenant();
		payTenant.setCreatedBy("test1");
		payTenant.setCreatedDate(new Date());
		payTenant.setLastModifiedBy("test1");
		payTenant.setLastModifiedDate(new Date());
		payTenant.setMemberDomain("test1");
		payTenant.setMemberId("test1");
		payTenant.setPlatformName("test1");
		payTenant.setTerminalId("test1");
		payTenant.setMerKey("test1");
		payUser = new PayUser();
		payUser.setId(1);
		payTenant.setUserId(payUser);
	}

	@Test
	public void testSaveS() {

		try {
			test_PayTenant = payTenantService.save(payTenant);

			id = test_PayTenant.getId();
			Assert.assertEquals(test_PayTenant.getMemberId(), "test1");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}

	}

	@Test
	public void testUpdateS() {

		payTenant.setId(id);
		payTenant.setMemberId("new_test1");
		try {

			test_PayTenant = payTenantService.update(payTenant);
			Assert.assertEquals(test_PayTenant.getMemberId(), "new_test1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testFindPK() {

		try {

			test_PayTenant = payTenantService.find(id);
			Assert.assertNotNull(test_PayTenant);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testFindAll() {

		try {

			List<PayTenant> list = payTenantService.findAll();
			Assert.assertTrue(list.size() >= 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDeletePK() {
		try {
			payTenantService.delete(id);
			test_PayTenant = payTenantService.find(id);
			Assert.assertNull(test_PayTenant);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

}
