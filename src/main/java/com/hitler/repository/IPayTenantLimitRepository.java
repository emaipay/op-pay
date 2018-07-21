package com.hitler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayTenantLimit;

public interface IPayTenantLimitRepository extends GenericRepository<PayTenantLimit, Integer> {
	
	@Query("from PayTenantLimit where tenantId=:connId and platformId=:platformId")
	public PayTenantLimit queryConnLimit(@Param("connId")Integer connId,@Param("platformId")Integer platformId);

	
	@Query("from PayTenantLimit where tenantId=:tenantId")
	public List<PayTenantLimit> queryConnLimitByTenantId(@Param("tenantId")Integer tenantId);
	
	@Modifying
	@Query("delete from PayTenantLimit where tenantId=:tenantId")
	public void deleteByTenantId(@Param("tenantId")Integer tenantId);

}
