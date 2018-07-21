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

import com.hitler.entity.PayRole;
import com.hitler.service.IPayRoleService;

/**
 * 角色管理
 * 
 * @author klp
 * @version 创建时间：2017年5月4日 下午5:37:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/web-ctx.xml" })
@FixMethodOrder(MethodSorters.DEFAULT) // 执行方法顺序 删除方法放最后
public class IPayRoleServiceTest {

	@Resource
	private IPayRoleService payRoleService;

	private PayRole payRole = null;// 初始实体
	private PayRole test_PayRole = null;// 测试后返回实体
	static int id = 0;// 测试使用id

	@Before
	public void setUp() throws Exception {
		payRole = new PayRole();
		payRole.setCreatedBy("test1");
		payRole.setCreatedDate(new Date());
		payRole.setLastModifiedBy("test1");
		payRole.setLastModifiedDate(new Date());
		payRole.setRoleName("test1");
		//payRole.setRoleType(RoleType.租户前台);
	}

	@Test
	public void testSaveS() {

		try {
			test_PayRole = payRoleService.save(payRole);
			id = test_PayRole.getId();
			Assert.assertEquals(test_PayRole.getRoleName(), "test1");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}

	}

	@Test
	public void testUpdateS() {

		payRole.setId(id);
		payRole.setRoleName("new_test1");

		try {
			test_PayRole = payRoleService.update(payRole);
			Assert.assertEquals(test_PayRole.getRoleName(), "new_test1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testFindPK() {

		try {

			test_PayRole = payRoleService.find(id);
			Assert.assertNotNull(test_PayRole);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testFindAll() {

		try {

			List<PayRole> list = payRoleService.findAll();
			Assert.assertTrue(list.size() >= 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDeletePK() {
		try {
			payRoleService.delete(id);
			test_PayRole = payRoleService.find(id);
			Assert.assertNull(test_PayRole);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

}
