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

import com.hitler.entity.PayPreference;
import com.hitler.service.IPayPreferenceService;

/**
 * 用户权限
 * 
 * @author klp
 * @version 创建时间：2017年5月4日 下午5:37:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/web-ctx.xml" })
@FixMethodOrder(MethodSorters.DEFAULT) // 执行方法顺序 删除方法放最后
public class IPayPreferenceServiceTest {

	@Resource
	private IPayPreferenceService payPreferenceService;

	private PayPreference payPreference = null;// 初始实体
	private PayPreference test_PayPreference = null;// 测试后返回实体
	static int id = 0;// 测试使用id

	@Before
	public void setUp() throws Exception {
		payPreference = new PayPreference();
		payPreference.setCreatedBy("test1");
		payPreference.setCreatedDate(new Date());
		payPreference.setLastModifiedBy("test1");
		payPreference.setLastModifiedDate(new Date());
		payPreference.setCode("test1");
		payPreference.setName("test1");
		payPreference.setValue("test1");
		payPreference.setRemark("test1");
	}

	@Test
	public void testSaveS() {

		try {
			test_PayPreference = payPreferenceService.save(payPreference);

			id = test_PayPreference.getId();
			Assert.assertEquals(test_PayPreference.getCode(), "test1");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}

	}

	@Test
	public void testUpdateS() {

		payPreference.setId(id);
		payPreference.setCode("new_test1");

		try {

			test_PayPreference = payPreferenceService.update(payPreference);
			Assert.assertEquals(test_PayPreference.getCode(), "new_test1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testFindPK() {

		try {

			test_PayPreference = payPreferenceService.find(id);
			Assert.assertNotNull(test_PayPreference);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testFindAll() {

		try {

			List<PayPreference> list = payPreferenceService.findAll();
			Assert.assertTrue(list.size() >= 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDeletePK() {
		try {
			payPreferenceService.delete(id);
			test_PayPreference = payPreferenceService.find(id);
			Assert.assertNull(test_PayPreference);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

}
