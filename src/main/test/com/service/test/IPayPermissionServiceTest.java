package com.service.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
* 权限分配
* @author klp
* @version 创建时间：2017年5月5日 下午5:18:39
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/web-ctx.xml" })
@FixMethodOrder(MethodSorters.DEFAULT) // 执行方法顺序 删除方法放最后
public class IPayPermissionServiceTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFindByPath() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindUserPermissionByUID() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByRoleId() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByUserId() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertRolePermission() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteRolePermission() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteByRoleId() {
		fail("Not yet implemented");
	}

	@Test
	public void testPermissionSave() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByCode() {
		fail("Not yet implemented");
	}

	@Test
	public void testPermissionBackTree() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByCodeIn() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindBySpec() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveS() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateS() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeletePK() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindPK() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAll() {
		fail("Not yet implemented");
	}

}
