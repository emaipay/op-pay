package com.hitler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.dto.user.PayRoleDTO;
import com.hitler.entity.PayRoleUser;

/**
 * 用户角色
 * @author jtwise
 *
 */
@SuppressWarnings("all")
public interface IPayRoleUserRepository extends GenericRepository<PayRoleUser, Integer> {

    /**
     * 通过用户id查找用户角色
     */
    String hql = "SELECT NEW com.hitler.dto.user.PayRoleDTO(ru.roleId.id,ru.roleId.roleName) FROM PayRoleUser ru WHERE ru.userId.id = :userId";
    @Query(hql)
    List<PayRoleDTO> getRolesByUserId(@Param("userId") Integer userId);

    @Query("FROM PayRoleUser a WHERE a.userId.id = :userId")
    public List<PayRoleUser> findByUserId(@Param("userId")Integer userId);

    @Modifying
    @Query("DELETE FROM PayRoleUser a WHERE a.userId.id = :userId")
    public int deleteByUserId(@Param("userId")Integer userId);

    @Query("FROM PayRoleUser a WHERE a.roleId.id = :roleId")
	List<PayRoleUser> findByRoleId(@Param("roleId")Integer roleId);
}
