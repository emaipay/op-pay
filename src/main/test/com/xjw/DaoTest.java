package com.xjw;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hitler.repository.IPayTenantMerchantBankRepository;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/web-ctx.xml"})
public class DaoTest {
	
	@Resource
	private IPayTenantMerchantBankRepository repository;
	
	@Test
	public void test(){
		repository.queryPayBankList(1, 1);
	}

}
