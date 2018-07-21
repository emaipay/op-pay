package com.hitler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayPlatformBank;

/**
* @author klp
* @version 创建时间：2017年5月3日 下午2:20:12
* 第三方支付平台对应支付方式配置JPA层
*/
public interface IPayPlatformBankRepository extends GenericRepository<PayPlatformBank, Integer> {

	 @Modifying
	    @Query("DELETE FROM PayPlatformBank  WHERE id in :ids")
	    public void deleteByIdList(@Param("ids") List<Integer> ids);
	 
	 @Query("from PayPlatformBank where payPlatformId.id=:payPlatfromId ")
	 public List<PayPlatformBank> queryByPlatformId(@Param("payPlatfromId")Integer payPlatformId);
}
