package com.hitler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.dto.user.PayRolePermissionDTO;
import com.hitler.entity.PayRolePermission;

/**
 * 角色权限
 * 
 * @author jtwise
 *
 */
public interface IPayRolePermissionRepository extends GenericRepository<PayRolePermission, Integer> {

	@Modifying
	@Query(value = "DELETE FROM t_pay_role_permission WHERE ROLE_ID = :roleId ", nativeQuery = true)
	int deleteByRoleId(@Param("roleId")Integer roleId);

	/**
	 * HUO
	 * 
	 * @param
	 * @return
	 */
	@Query("SELECT NEW com.hitler.dto.user.PayRolePermissionDTO(rp) FROM PayRolePermission rp where rp.roleId.id= :roleId")
	List<PayRolePermissionDTO> getRolePermissions(@Param("roleId") Integer roleId);

}
