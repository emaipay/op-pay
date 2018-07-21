package com.service.test;

import static org.junit.Assert.fail;

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

import com.hitler.entity.PayPermission;
import com.hitler.entity.PayRole;
import com.hitler.entity.PayRolePermission;
import com.hitler.service.IPayRolePermissionService;

/**
 * 权限资源管理
 * 
 * @author klp
 * @version 创建时间：2017年5月4日 下午5:37:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/web-ctx.xml" })
@FixMethodOrder(MethodSorters.DEFAULT) // 执行方法顺序 删除方法放最后
public class IPayRolePermissionServiceTest {

	@Resource
	private IPayRolePermissionService payRolePermissionService;

	private PayRole payRole = null;
	private PayPermission payPermission = null;
	private PayRolePermission payRolePermission = null;// 初始实体
	private PayRolePermission test_PayRolePermission = null;// 测试后返回实体
	static int id = 0;// 测试使用id

	@Before
	public void setUp() throws Exception {
		payRolePermission = new PayRolePermission();
		payRole = new PayRole();
		payRole.setId(1);
		payRolePermission.setRoleId(payRole);
		payPermission = new PayPermission();
		payPermission.setId(1);
		payRolePermission.setPermissionId(payPermission);
	}

	@Test
	public void testSaveS() {

		try {
			test_PayRolePermission = payRolePermissionService.save(payRolePermission);
			id = test_PayRolePermission.getId();
			Assert.assertEquals(test_PayRolePermission.getRoleId().getId().toString(), "1");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}

	}

	@Test
	public void testUpdateS() {

		payRolePermission.setId(id);
		payRole = new PayRole();
		payRole.setId(2);
		payRolePermission.setPermissionId(payPermission);

		try {
			test_PayRolePermission = payRolePermissionService.update(payRolePermission);
			Assert.assertEquals(test_PayRolePermission.getRoleId().getId().toString(), 2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testFindPK() {

		try {

			test_PayRolePermission = payRolePermissionService.find(id);
			Assert.assertNotNull(test_PayRolePermission);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testFindAll() {

		try {

			List<PayRolePermission> list = payRolePermissionService.findAll();
			Assert.assertTrue(list.size() >= 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDeletePK() {
		try {
			payRolePermissionService.delete(id);
			test_PayRolePermission = payRolePermissionService.find(id);
			Assert.assertNull(test_PayRolePermission);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

}
