package com.hitler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.dto.user.PayPermissionDTO;
import com.hitler.entity.PayPermission;
import com.hitler.entity.PayRole;

/**
 * 用户权限
 * @author jtwise
 *
 */
public interface IPayPermissionRepository extends GenericRepository<PayPermission, Integer> {

	/*List<Permission> findByUID(Integer uid);
	
	*//**
	 * 查找所有用户有权限的路径
	 * @param uid
	 * @return
	 *//*
	Set<String> findPathByUID(Integer uid);
	*/

	/**
	 *
	 * @param
	 * @return
     */
	@Query("from PayPermission p")
	List<PayPermission> findPermissionTree();

	/**
	 * 通过权限code查找
	 * @return
     */
	PayPermission findByCode(String code);

	/**
	 * 通过roleId查找权限
	 * @return
	 */
	String hql = "SELECT NEW com.hitler.dto.user.PayPermissionDTO(rp.permissionId.id,rp.permissionId.permissionName) FROM PayRolePermission rp WHERE rp.roleId = :roleId";
	@Query(hql)
	List<PayPermissionDTO> findByRoleId(@Param("roleId") PayRole roleId);
}
