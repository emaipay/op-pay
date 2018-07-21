package com.hitler.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitler.core.repository.GenericRepository;
import com.hitler.entity.PayUser;

/**
 * 用户管理JPA层
 */
public interface IPayUserRepository extends GenericRepository<PayUser, Integer> {

    /**
     * 查找用户
     *
     * @param tenantCode 租户
     * @param userName   用户名
     * @return
     */
	@Query("from PayUser where userName=:userName")
    public PayUser findByUserName(@Param("userName")String userName);

    /**
     * 扣款/支付s
     */
    @Modifying
    @Query(value = "UPDATE T_pay_USER u SET u.BALANCE = u.BALANCE - :BetAmount WHERE u.id = :userId  AND u.BALANCE >= :BetAmount", nativeQuery = true)
    public int pay(@Param("userId") Integer userId, @Param("BetAmount") Double BetAmount);


    /**
     * 账户锁定/删除-批量
     *
     * @param ids
     * @param accountStatus
     */
    @Modifying
    @Query("UPDATE PayUser SET accountLocked = :accountStatus WHERE id in :ids")
    public void batchAccount(@Param("ids") List<Integer> ids, @Param("accountStatus") PayUser.AccountStatus accountStatus);
    
    List<PayUser> findByIdIn(Collection<Integer> ids);
    
    @Query("from PayUser where accountLocked=0 and accountType=1 and loginLocked=1")
    public List<PayUser> queryNormalUser();
    
    @Modifying
    @Query("UPDATE PayUser  u SET u.pwdLogin= :pwdLogin WHERE id = :usId")
    int updatePwdLogin(@Param("usId") Integer usId, @Param("pwdLogin") String pwdLogin);


    
}
