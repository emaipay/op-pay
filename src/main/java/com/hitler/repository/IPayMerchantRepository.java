package com.hitler.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayMerchant;

public interface IPayMerchantRepository extends GenericRepository<PayMerchant, Integer> {
	
	/**
	 * 修改商户表余额
	 * @param money
	 * @param merchantId
	 * @return
	 */
	@Modifying
	@Query("update PayMerchant t set t.currentBalance=t.currentBalance + :money where t.id=:merchantId ")
	public int updatecurrentBalance(@Param("money")Double money,@Param("merchantId")Integer merchantId);

}
