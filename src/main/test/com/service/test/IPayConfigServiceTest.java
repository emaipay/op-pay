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

import com.hitler.entity.PayConfig;
import com.hitler.entity.PayConfig.MethodType;
import com.hitler.service.IPayConfigService;

/**
 * 各支付平台配置表
 * 
 * @author klp
 * @version 创建时间：2017年5月4日 下午5:37:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/web-ctx.xml" })
@FixMethodOrder(MethodSorters.DEFAULT) // 执行方法顺序 删除方法放最后
public class IPayConfigServiceTest {

	@Resource
	private IPayConfigService payConfigService;

	private PayConfig payConfig = null;// 初始实体
	private PayConfig test_PayConfig = null;// 测试后返回实体
	static int id = 0;// 测试使用id

	@Before
	public void setUp() throws Exception {
		payConfig = new PayConfig();
		payConfig.setCreatedBy("test1");
		payConfig.setCreatedDate(new Date());
		payConfig.setLastModifiedBy("test1");
		payConfig.setLastModifiedDate(new Date());
		payConfig.setPlatformType("test1");
		payConfig.setClassPath("test1");
		payConfig.setClassMethod("test1");
		payConfig.setMethodType(MethodType.单号获取);
	}

	@Test
	public void testSaveS() {
	
		try {
			test_PayConfig = payConfigService.save(payConfig);

			id = test_PayConfig.getId();
			Assert.assertEquals(test_PayConfig.getPlatformType(), "test1");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}

	}

	@Test
	public void testUpdateS() {

	 	payConfig.setId(id);
	    payConfig.setPlatformType("new_test1");
     try {

			test_PayConfig = payConfigService.update(payConfig);
			Assert.assertEquals(test_PayConfig.getPlatformType(), "new_test1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testFindPK() {

		try {

			test_PayConfig = payConfigService.find(id);
			Assert.assertNotNull(test_PayConfig);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testFindAll() {

		try {

			List<PayConfig> list = payConfigService.findAll();
			Assert.assertTrue(list.size() >= 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDeletePK() {
		try {
			payConfigService.delete(id);
			test_PayConfig = payConfigService.find(id);
			Assert.assertNull(test_PayConfig);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

}
