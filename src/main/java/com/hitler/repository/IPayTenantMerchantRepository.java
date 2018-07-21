package com.hitler.repository;

 
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayTenantMerchant;

/**
*接入商户对应第三方支付商户JPA层
* @author klp
* @version 创建时间：2017年5月3日 下午3:01:44
*/
public interface IPayTenantMerchantRepository extends GenericRepository<PayTenantMerchant, Integer> {
	@Query("from PayTenantMerchant where tenantId.memberId=:memberId and tenantId.terminalId=:terminalId and tenantId.available='1'")
	public List<PayTenantMerchant> queryPayConnMerchant(@Param("memberId")String memberId,@Param("terminalId")String terminalId);
	
	@Query("FROM PayTenantMerchant a WHERE a.tenantId.id = :tenantId  and a.tenantId.available='1'")
	public List<PayTenantMerchant> findByTenantId(@Param("tenantId")Integer tenantId);
	
	@Modifying
	@Query("DELETE FROM PayTenantMerchant a WHERE a.tenantId.id = :tenantId")
	public int deletetenantId(@Param("tenantId")Integer tenantId);
}
