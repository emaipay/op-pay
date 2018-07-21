package com.hitler.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayRole;

/**
 * 用户角色
 * @author jtwise
 *
 */
public interface IPayRoleRepository extends GenericRepository<PayRole, Integer> {

    /**
     *
     * @param
     * @return
     */
    @Modifying
    @Query(value = "DELETE FROM t_pay_permission WHERE TENANT_CODE = ?1 ", nativeQuery = true)
    public int deleteByTenantCode(String TenantCode);

	public PayRole findByRoleName(String roleName);
	
	List<PayRole> findByIdIn(Collection<Integer> ids);
}
