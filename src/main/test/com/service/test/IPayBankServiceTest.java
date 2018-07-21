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

import com.hitler.entity.PayBank;
import com.hitler.entity.PayBank.AllowBindingStatus;
import com.hitler.entity.PayBank.BankTransferStatus;
import com.hitler.service.IPayBankService;

/**
 * 支付平台可支付方式(银行卡)
 * 
 * @author klp
 * @version 创建时间：2017年5月4日 下午5:37:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/web-ctx.xml" })
@FixMethodOrder(MethodSorters.DEFAULT) // 执行方法顺序 删除方法放最后
public class IPayBankServiceTest {

	@Resource
	private IPayBankService payBankService;

	private PayBank payBank = null;// 初始实体
	private PayBank test_PayBank = null;// 测试后返回实体
	static int id = 0;// 测试使用id

	@Before
	public void setUp() throws Exception {
		payBank = new PayBank();
		payBank.setCreatedBy("test1");
		payBank.setCreatedDate(new Date());
		payBank.setLastModifiedBy("test1");
		payBank.setLastModifiedDate(new Date());
		payBank.setAllowBindingStatus(AllowBindingStatus.开放);
		payBank.setBankTransferStatus(BankTransferStatus.开放);
		payBank.setLogoFilePath("test1");
		payBank.setName("test1");
		payBank.setShortName("test1");
	}

	@Test
	public void testSaveS() {

		try {
			test_PayBank = payBankService.save(payBank);

			id = test_PayBank.getId();
			Assert.assertEquals(test_PayBank.getName(), "test1");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}

	}

	@Test
	public void testUpdateS() {

		payBank.setId(id);
		payBank.setName("new_test1");

		try {

			test_PayBank = payBankService.update(payBank);
			Assert.assertEquals(test_PayBank.getName(), "new_test1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testFindPK() {

		try {

			test_PayBank = payBankService.find(id);
			Assert.assertNotNull(test_PayBank);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testFindAll() {

		try {

			List<PayBank> list = payBankService.findAll();
			Assert.assertTrue(list.size() >= 0);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDeletePK() {
		try {
			payBankService.delete(id);
			test_PayBank = payBankService.find(id);
			Assert.assertNull(test_PayBank);

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

}
