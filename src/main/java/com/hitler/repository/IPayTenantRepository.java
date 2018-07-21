package com.hitler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayTenant;
import com.hitler.entity.PayTenantMerchant;

/**
*接入商户JPA层
* @author klp
* @version 创建时间：2017年5月3日 下午2:24:24
*/
public interface IPayTenantRepository extends GenericRepository<PayTenant, Integer> {
	@Query("from PayTenantMerchant where tenantId.memberId=:memberId and tenantId.terminalId=:terminalId")
	public List<PayTenantMerchant> queryPayConnMerchant(@Param("memberId")String memberId,@Param("terminalId")String terminalId);

	
	   @Modifying
	    @Query("DELETE FROM PayTenant  WHERE id in :ids")
	    public void deleteByIdList(@Param("ids") List<Integer> ids);
	   
	   @Query("from PayTenant where userId.id=:userId")
	   public List<PayTenant> queryTenantByUserId(@Param("userId")Integer userId);
	   
	    /**
	     * 查找租户
	     *
	     * @param memberId 
	     * @param terminalId 
	     * @return
	     */
		@Query("from PayTenant where memberId=:memberId and terminalId=:terminalId")
	    public PayTenant queryPayTenant(@Param("memberId")String memberId, @Param("terminalId")String terminalId);
	
}
