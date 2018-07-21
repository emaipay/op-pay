package com.hitler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayTenantMerchantBank;

/**
 * 接入商户对应商户的支付方式(银行卡) JPA层
 * 
 * @author klp
 * @version 创建时间：2017年5月3日 下午3:01:44
 */
public interface IPayTenantMerchantBankRepository extends GenericRepository<PayTenantMerchantBank, Integer> {

	@Query("from PayTenantMerchantBank where payMerchantId.id=:merchantId and payTenantId.id=:connId")
	List<PayTenantMerchantBank> queryPayBankList(@Param("connId") Integer connId,
			@Param("merchantId") Integer merchantId);

	@Query(value="select pay_bank_id from t_pay_tenant_merchant_bank where pay_merchant_id=:merchantId and TENANT_ID=:connId",nativeQuery=true)
	List<Integer> queryPayBankIdList(@Param("connId") Integer connId,
			@Param("merchantId") Integer merchantId);
	@Modifying
	@Query("DELETE FROM PayTenantMerchantBank  WHERE id in :ids")
	public void deleteByIdList(@Param("ids") List<Integer> ids);

	@Query("FROM PayTenantMerchantBank a WHERE a.payTenantId.id = :tenantId")
	public List<PayTenantMerchantBank> findByTenantId(@Param("tenantId") Integer tenantId);
	
	@Modifying
	@Query("DELETE FROM PayTenantMerchantBank a WHERE a.payTenantId.id = :tenantId")
	public int deleteByTenantIdId(@Param("tenantId") Integer tenantId);

}
