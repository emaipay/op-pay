package com.hitler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayPlatform;
import com.hitler.entity.PayPlatformBank;

/**
 * 
 * @author jt-x
 * 2016-03-01
 */
public interface IPayPlatformRepository extends GenericRepository<PayPlatform, Integer>{
	
	/**
	 * 查找支付平台对应银行的功能id
	 */
	@Query("from PayPlatformBank where bankId.id=:bankId and payPlatformId.id=:platformId")
	public PayPlatformBank queryPayPlatformBankCode(@Param("bankId")Integer bankId,@Param("platformId")Integer platformId);

	   @Modifying
	    @Query("DELETE FROM PayPlatform  WHERE id in :ids")
	    public void deleteByIdList(@Param("ids") List<Integer> ids);
}
